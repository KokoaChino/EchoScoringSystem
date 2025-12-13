package com.echo.external.service.impl;

import com.echo.external.service.api.KujiequApiService;
import com.echo.external.dto.CharacterDetailDTO;
import com.echo.external.dto.WeaponDetailDTO;
import com.echo.external.util.KujiequApiHttpUtil;
import com.echo.external.util.KujiequApiParseUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service
public class KujiequApiServiceImpl implements KujiequApiService {

    private static final String PAGE_URL = "https://api.kurobbs.com/wiki/core/catalogue/item/getPage";
    private static final String ENTRY_DETAIL_URL = "https://api.kurobbs.com/wiki/core/catalogue/item/getEntryDetail";
    private static final Pattern ID_PATTERN = Pattern.compile("/(\\d+)(?:\\s*|[/?#].*)?$");

    @Resource
    private KujiequApiHttpUtil util;

    @Override
    public List<WeaponDetailDTO> getWeaponList() {
        JsonNode data = util.postAndGetBusinessData(
                PAGE_URL,
                new FormBody.Builder()
                        .add("catalogueId", "1106")
                        .add("limit", "1000")
        );
        List<WeaponDetailDTO> res = new ArrayList<>();
        for (JsonNode node : data.path("results").path("records")) {
            JsonNode content = node.path("content");
            int star = content.path("star").asInt();
            boolean showTeaserIcon = content.path("showTeaserIcon").asBoolean(true);
            int teaserIconNum = content.path("showTeaserIconNum").asInt(-1);
            if (showTeaserIcon && teaserIconNum == 2) {
                continue;
            }
            String id = node.path("entryId").asText();
            if (StringUtil.isBlank(id) || id.equals("null")) {
                String linkUrl = content.path("linkUrl").asText();
                Matcher m = ID_PATTERN.matcher(linkUrl);
                if (m.find()) {
                    id = m.group(1);
                } else {
                    continue;
                }
            }
            WeaponDetailDTO dto = new WeaponDetailDTO();
            dto.setId(id);
            dto.setStar(star);
            res.add(dto);
        }
        return res;
    }

    @Override
    public WeaponDetailDTO enrichWeaponDetail(WeaponDetailDTO dto) {
        JsonNode data = util.postAndGetBusinessData(
                ENTRY_DETAIL_URL,
                new FormBody.Builder().add("id", dto.getId())
        );
        String id = data.get("id").asText();
        String name = data.get("name").asText();
        JsonNode modules = data.path("content").path("modules");
        JsonNode baseInfoComponent = null;
        for (JsonNode module : modules) {
            JsonNode components = module.path("components");
            if (components.isArray() && !components.isEmpty()) {
                baseInfoComponent = components.get(0);
                break;
            }
        }
        if (baseInfoComponent == null) {
            throw new RuntimeException("未找到基础信息组件");
        }
        String baseHtml = baseInfoComponent.path("content").asText();
        String weaponType = KujiequApiParseUtil.extractTextByLabel(baseHtml, "武器类型");
        String imageUrl = KujiequApiParseUtil.extractFirstImageUrl(baseHtml);
        JsonNode detailPanelComponent = null;
        for (JsonNode module : modules) {
            JsonNode components = module.path("components");
            for (JsonNode comp : components) {
                if ("详细面板".equals(comp.path("title").asText())) {
                    detailPanelComponent = comp;
                    break;
                }
            }
            if (detailPanelComponent != null) break;
        }
        if (detailPanelComponent == null) {
            throw new RuntimeException("未找到详细面板组件");
        }
        int maxAtk = 0;
        JsonNode tabs = detailPanelComponent.path("tabs");
        for (JsonNode tab : tabs) {
            if ("90级".equals(tab.path("title").asText())) {
                String html = tab.path("content").asText();
                maxAtk = KujiequApiParseUtil.extractAtkFromHtml(html);
                break;
            }
        }
        return new WeaponDetailDTO(id, name, weaponType, dto.getStar(), maxAtk, imageUrl);
    }

    @Override
    public List<CharacterDetailDTO> getCharacterList() {
        JsonNode data = util.postAndGetBusinessData(
                PAGE_URL,
                new FormBody.Builder()
                        .add("catalogueId", "1105")
                        .add("limit", "1000")
        );
        List<CharacterDetailDTO> res = new ArrayList<>();
        for (JsonNode node : data.path("results").path("records")) {
            JsonNode content = node.path("content");
            boolean showTeaserIcon = content.path("showTeaserIcon").asBoolean(true);
            int teaserIconNum = content.path("showTeaserIconNum").asInt(-1);
            if (showTeaserIcon && teaserIconNum == 2) {
                continue;
            }
            CharacterDetailDTO dto = new CharacterDetailDTO();
            dto.setId(node.path("entryId").asText());
            dto.setStar(content.path("star").asInt());
            dto.setName(node.path("name").asText());
            res.add(dto);
        }
        return res;
    }

    @Override
    public CharacterDetailDTO enrichCharacterDetail1(CharacterDetailDTO dto) {
        JsonNode data = util.postAndGetBusinessData(
                ENTRY_DETAIL_URL,
                new FormBody.Builder().add("id", dto.getId())
        );
        CharacterDetailDTO res = new CharacterDetailDTO();
        res.setStar(dto.getStar());
        res.setId(data.path("id").asText());
        res.setName(data.path("content").path("title").asText());
        JsonNode infoArray = data
                .path("content").path("modules").path(0)
                .path("components").path(0)
                .path("role").path("info");
        if (infoArray.isArray()) {
            for (JsonNode item : infoArray) {
                String text = item.path("text").asText("");
                if (text.startsWith("属性：")) {
                    res.setType(text.split("：", 2)[1]);
                    break;
                }
            }
        }
        JsonNode figures = data
                .path("content").path("modules").path(0)
                .path("components").path(0)
                .path("role").path("figures");
        if (figures.isArray()) {
            for (JsonNode fig : figures) {
                if ("编队立绘".equals(fig.path("name").asText())) {
                    res.setSquadFigureUrl(fig.path("url").asText());
                    break;
                }
            }
        }
        JsonNode components = data.path("content").path("modules").path(0).path("components");
        JsonNode tabsNode = null;
        if (components.isArray()) {
            for (JsonNode comp : components) {
                if ("tabs-component".equals(comp.path("type").asText())) {
                    tabsNode = comp.path("tabs");
                    break;
                }
            }
        }
        String level90Html = null;
        if (tabsNode != null) {
            for (JsonNode tab : tabsNode) {
                String title = tab.path("title").asText("").trim();
                String content = tab.path("content").asText();
                if ("90".equals(title) || (content != null && content.contains("满突破"))) {
                    level90Html = content;
                    if ("90".equals(title)) {
                        break;
                    }
                }
            }
        }
        if (level90Html != null && !level90Html.trim().isEmpty()) {
            try {
                res.setMaxHp(KujiequApiParseUtil.extractStatFromTable(level90Html, "基础生命"));
                res.setMaxAtk(KujiequApiParseUtil.extractStatFromTable(level90Html, "基础攻击"));
                res.setMaxDef(KujiequApiParseUtil.extractStatFromTable(level90Html, "基础防御"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    @Override
    public List<CharacterDetailDTO> enrichCharacterIds(List<CharacterDetailDTO> dtoList) {
        Map<String, CharacterDetailDTO> map = dtoList.stream()
                .filter(dto -> dto.getName() != null)
                .collect(Collectors.toMap(
                        CharacterDetailDTO::getName, Function.identity()
                ));
        JsonNode characters = util.getBusinessData("https://guide-server.aki-game.com/role/avatar/list");
        for (JsonNode role : characters) {
            String idStr = role.path("roleGbId").asText();
            if (idStr.isEmpty()) continue;
            String zhName = null;
            JsonNode texts = role.path("texts");
            if (texts.isArray()) {
                for (JsonNode textObj : texts) {
                    if ("zh-Hans".equals(textObj.path("language").asText())) {
                        zhName = textObj.path("name").asText("#");
                        break;
                    }
                }
            }
            if (map.containsKey(zhName)) {
                map.get(zhName).setId(idStr);
            } else if (zhName.startsWith("漂泊者·")) {
                String type = zhName.substring("漂泊者·".length()).trim();
                String male = "漂泊者-男-" + type, female = "漂泊者-女-" + type;
                if (map.containsKey(male)) {
                    map.get(male).setId(idStr);
                }
                if (map.containsKey(female)) {
                    map.get(female).setId(idStr);
                }
            }
        }
        return dtoList;
    }

    @Override
    public CharacterDetailDTO enrichCharacterDetail2(CharacterDetailDTO dto) {
        String roleGbId = dto.getId();
        JsonNode listData = util.getBusinessData(
                "https://guide-server.aki-game.com/introduction/list?roleGbId=" + roleGbId
        );
        JsonNode firstIntro = listData.get(0);
        String introIdStr = firstIntro.path("id").asText();
        JsonNode infoData = util.getBusinessData(
                String.format(
                        "https://guide-server.aki-game.com/introduction/info?roleGbId=%s&id=%s",
                        roleGbId, introIdStr
                )
        );
        dto.setAvatarUrl(infoData.path("role").path("cardPictureUrl").asText());
        JsonNode weaponItems = infoData.path("weapon").path("items");
        if (weaponItems.isArray() && !weaponItems.isEmpty()) {
            JsonNode firstWeapon = weaponItems.get(0);
            JsonNode texts = firstWeapon.path("texts");
            if (texts.isArray() && !texts.isEmpty()) {
                dto.setExclusiveWeapon(texts.get(0).path("name").asText());
            }
        }
        return dto;
    }
}