package com.echo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.echo.dto.EchoPair;
import com.echo.dto.RoleDTO;
import com.echo.entity.*;
import com.echo.entity.Character;
import com.echo.mapper.EchoMapper;
import com.echo.mapper.persistence.CharacterConfigDO;
import com.echo.mapper.persistence.EchoDO;
import com.echo.service.api.EchoScoringSystemService;
import com.echo.util.EchoConvert;
import com.echo.util.EchoUtil;
import jakarta.annotation.Resource;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;


@Service
public class EchoScoringSystemServiceImpl implements EchoScoringSystemService {

    @Resource
    private EchoMapper mapper;

    @Resource
    private EchoUtil echoUtil;

    private static final String[] ECHO_KEYS = EchoUtil.ECHO_KEYS; // 副词条名称
    private static final Map<String, double[]> ECHO_VALUES = EchoUtil.ECHO_VALUES; // 副词条取值
    private static final Map<String, Double> ECHO_AVERAGE = EchoUtil.ECHO_AVERAGE; // 副词条基准值

    @Override
    public String[] getEchoKeys() { // 获取副词条名称
        return ECHO_KEYS;
    }
    @Override
    public Map<String, double[]> getEchoValues() { // 获取副词条取值
        return ECHO_VALUES;
    }
    @Override
    public Map<String, Double> getEchoAverage() { // 获取副词条基准值
        return ECHO_AVERAGE;
    }
    @Override
    public Map<String, Weapon> getWeapons() { // 获取武器
        return echoUtil.getWeapons();
    }

    @Override
    public List<Weapon> getWeaponsByScreen(String json) { // 获取筛选后的武器
        Map<String, List<String>> screen = JSON.parseObject(json, new TypeReference<Map<String, List<String>>>() {});
        List<Weapon> res = new ArrayList<>();
        Set<String> types = new HashSet<>(screen.get("类型"));
        Set<String> lvs = new HashSet<>(screen.get("星级"));
        for (Weapon weapon : getWeapons().values()) {
            if (lvs.contains(String.valueOf(weapon.getStar())) && types.contains(weapon.getType())) {
                res.add(weapon);
            }
        }
        return res;
    }

    @Override
    public void setWeapon(String username, String name, String weapon) { // 设置角色武器
        mapper.updateWeapon(username, name, weapon);
        if (weapon.equals(echoUtil.getCharacters().get(name).getWeapon().getName()))
            mapper.deleteWeapon(username, name);
    }

    @Override
    public Map<String, Character> getCharacters(String username) { // 获取角色
        Map<String, Character> res = echoUtil.getCharacters();
        for (Character c: res.values()) {
            String weapon = mapper.selectWeapon(username, c.getName());
            if (weapon != null) {
                c.setWeapon(echoUtil.getWeapons().get(weapon));
            }
        }
        return res;
    }

    @Override
    public Map<String, Character> getCharactersByScreen(String username, String json) { // 获取筛选后的角色
        List<String> names = JSON.parseObject(json, new TypeReference<List<String>>() {});
        Map<String, Character> characters = getCharacters(username);
        if (names.isEmpty()) return characters;
        Map<String, Character> res = new LinkedHashMap<>();
        for (String name : names) {
            res.put(name, characters.get(name));
        }
        return res;
    }

    @Override
    public List<String> getNames() { // 获取角色名称列表
        return echoUtil.getCharacters().keySet().stream().sorted(
                Comparator.comparing(c -> echoUtil.getPinyin(c))).toList();
    }

    @Override
    public Map<String, List<String>> getCharacterGroupsByType() { // 获取角色分组
        Map<String, List<String>> res = new LinkedHashMap<>(){{
            put("气动", new ArrayList<>());
            put("导电", new ArrayList<>());
            put("冷凝", new ArrayList<>());
            put("热熔", new ArrayList<>());
            put("衍射", new ArrayList<>());
            put("湮灭", new ArrayList<>());
        }};
        for (Character character : echoUtil.getCharacters().values()) {
            res.get(character.getType()).add(character.getName());
        }
        for (List<String> list : res.values()) {
            list.sort(Comparator.comparing(a -> echoUtil.getCharacters().get(a).getPinyin()));
        }
        return res;
    }

    @Override
    public Map<String, int[]> getCharacterStats() { // 获取角色三维属性
        Map<String, int[]> res = new LinkedHashMap<>();
        for (Character character: echoUtil.getCharacters().values()) {
            res.put(character.getName(), character.getStats());
        }
        return res;
    }

    @Override
    public Map<String, ? extends Number> getweightsByUsername(String username, String name) { // 获取角色完整副词条权重
        String json = mapper.getWeight(username, name);
        if (json == null) {
            String weapon = mapper.selectWeapon(username, name);
            if (weapon == null) weapon = echoUtil.getCharacters().get(name).getWeapon().getName();
            return echoUtil.getweights(name, weapon);
        }
        return JSON.parseObject(json, new TypeReference<Map<String, ? extends Number>>() {});
    }

    @Override
    public Map<String, Map<String, ? extends Number>> getAllweightsByUsername(String username) { // 获取所有角色完整副词条权重
        List<CharacterConfigDO> dbResults = mapper.getAllWeightsByUsername(username);
        Map<String, Map<String, ? extends Number>> res = new HashMap<>();
        for (CharacterConfigDO configDO : dbResults) {
            res.put(
                    configDO.getName(),
                    JSON.parseObject(configDO.getWeight(), new TypeReference<Map<String, ? extends Number>>() {})
            );
        }
        List<String> names = getNames();
        Map<String, String> weapons = new HashMap<>();
        for (String name : names) {
            if (!res.containsKey(name)) {
                if (weapons.isEmpty()) {
                    dbResults = mapper.getAllWeaponsByUsername(name);
                    for (CharacterConfigDO configDO : dbResults) {
                        weapons.put(configDO.getName(), configDO.getWeapon());
                    }
                }
                String weapon = weapons.get(name);
                if (weapon == null) weapon = echoUtil.getCharacters().get(name).getWeapon().getName();
                res.put(name, echoUtil.getweights(name, weapon));
            }
        }
        return res;
    }

    @Override
    public void setweights(String username, String name, String json) { // 设置角色副词条权重
        int i = mapper.updateWeight(username, name, json);
        if (i == 0) mapper.insertWeight(username, name, json);
    }

    @Override
    public void reweights(String username, String name) { // 重置角色副词条权重
        mapper.deleteWeight(username, name);
    }

    @Override
    public Map<String, Number> getEchoPercent(String username, String name, String json) { // 获取声骸评分
        Map<String, Number> values = JSON.parseObject(json, new TypeReference<Map<String, Number>>() {});
        Map<String, ? extends Number> weights = getweightsByUsername(username, name); // 角色完整副词条权重
        Map<String, Number> res = new LinkedHashMap<>();
        List<Map.Entry<String, Double>> pairs = new ArrayList<>();
        for (String key: ECHO_KEYS) {
            double value = weights.get(key).doubleValue() * ECHO_VALUES.get(key)[0] / ECHO_AVERAGE.get(key);
            pairs.add(new AbstractMap.SimpleEntry<>(key, value));
        }
        pairs.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        double maxScore = pairs.stream().limit(5).mapToDouble(Map.Entry::getValue).sum(); // 理论最大分数
        double myScore = 0; // 实际分数
        for (String key: values.keySet()) {
            double score = weights.get(key).doubleValue() * values.get(key).doubleValue() / ECHO_AVERAGE.get(key);
            myScore += score;
            res.put(key, score);
        }
        for (String key: values.keySet()) {
            res.put(key, (int) Math.round(res.get(key).doubleValue() * 100 / myScore)); // 每个副词条对总得分的贡献百分比
        }
        res.put("总得分", (int) Math.round(myScore * 100 / maxScore)); // 总得分
        return res;
    }

    @Override
    public Map<String, List<Echo>> getData(String username) { // 获取角色词条数据
        Map<String, List<Echo>> data = new HashMap<>();
        List<EchoPair> list = mapper.getEchoAndName(username);
        if (list != null) {
            for (EchoPair pair: list) {
                String name = pair.getName(), json = pair.getEcho();
                Echo echo = JSON.parseObject(json, new TypeReference<Echo>() {});
                data.computeIfAbsent(name, k -> new ArrayList<>()).add(echo);
            }
        }
        for (List<Echo> li: data.values()) {
            int size = li.size();
            for (int i = 0; i < 5 - size; i++) {
                li.add(Echo.getEmpty());
            }
        }
        EchoUtil.sortData(data);
        return data;
    }

    @Override
    public Map<String, List<Echo>> getDataByScreen(String username, String json) { // 获取筛选后的角色词条数据
        Map<String, List<String>> check = JSON.parseObject(json, new TypeReference<Map<String, List<String>>>() {});
        Set<String> name = new HashSet<>(check.get("name"));
        Set<String> cost = new HashSet<>(check.get("cost"));
        Set<String> main = new HashSet<>(check.get("main"));
        Set<String> echo = new HashSet<>(check.get("echo"));
        int lower = Integer.parseInt(check.get("range").get(0)), upper = Integer.parseInt(check.get("range").get(1));
        Map<String, List<Echo>> data = getData(username);
        for (Map.Entry<String, List<Echo>> entry: new HashSet<>(data.entrySet())) {
            if (!name.isEmpty() && !name.contains(entry.getKey())) {
                data.remove(entry.getKey());
                continue;
            }
            List<Echo> list = entry.getValue();
            for (int i = 0; i < list.size(); i++) {
                Echo e = list.get(i);
                if (e.getScore().isEmpty()) continue;
                double score = Double.parseDouble(e.getScore());
                if (score < lower || score > upper || (!cost.isEmpty() && !cost.contains(e.getCost())) ||
                        (!main.isEmpty() && !main.contains(e.getMain().substring(0, e.getMain().indexOf(' '))))) {
                    list.set(i, Echo.getEmpty());
                    continue;
                }
                if (!echo.isEmpty()) {
                    boolean found = false;
                    for (List<String> li : e.getEcho()) {
                        if (echo.contains(li.get(0))) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        list.set(i, Echo.getEmpty());
                    }
                }
            }
        }
        for (String key: new HashSet<>(data.keySet())) {
            boolean found = false;
            for (Echo e : data.get(key)) {
                if (!e.getScore().isEmpty()) {
                    found = true;
                    break;
                }
            }
            if (!found) data.remove(key);
        }
        EchoUtil.sortData(data);
        return data;
    }

    @Override
    public Map<String, List<Echo>> addData(String username, String json, String name) { // 添加角色声骸数据
        Map<String, List<Echo>> data = getData(username);
        Echo echo = JSON.parseObject(json, Echo.class);
        if (!data.containsKey(name)) {
            List<Echo> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(Echo.getEmpty());
            }
            data.put(name, list);
        }
        List<Echo> list = data.get(name);
        for (int i = 0; i < 5; i++) {
            if (list.get(i).getCost().isEmpty()) {
                list.set(i, echo);
                break;
            }
        }
        EchoUtil.sortData(data);
        mapper.insertEcho(username, name, json);
        return data;
    }

    @Override
    public Map<String, List<Echo>> delEcho(String username, Integer index, String name) { // 删除角色声骸数据
        Map<String, List<Echo>> data = getData(username);
        List<Echo> list = data.get(name);
        String json = JSON.toJSONString(list.get(index));
        list.set(index, Echo.getEmpty());
        for (int i = 0, cnt = 0; i < 5; i++) {
            if (list.get(i).getCost().isEmpty()) {
                cnt++;
            }
            if (cnt == 5) {
                data.remove(name);
            }
        }
        EchoUtil.sortData(data);
        Echo tmp = JSON.parseObject(json, Echo.class);
        for (EchoPair echoPair: mapper.getEchoAndId(username, name)) {
            if (JSON.parseObject(echoPair.getEcho(), Echo.class).equals(tmp)) {
                mapper.deleteEchoById(echoPair.getId());
                break;
            }
        }
        return data;
    }

    @Override
    public Map<String, List<Echo>> reData(String username) { // 重新计算角色声骸数据
        Map<String, List<Echo>> data = getData(username); // 角色 -> 声骸列表
        for (String name: data.keySet()) {
            for (Echo echo: data.get(name)) {
                if (echo.getScore().isEmpty()) break;
                Map<String, Number> values = new LinkedHashMap<>();
                for (List<String> li: echo.getEcho()) {
                    if (li.get(0).isEmpty()) continue;
                    values.put(li.get(0), Double.valueOf(li.get(1)));
                }
                String json = JSON.toJSONString(values);
                Map<String, Number> score = getEchoPercent(username, name, json); // 声骸副词条 -> 分数占比
                for (List<String> li: echo.getEcho()) {
                    li.set(2, score.get(li.get(0)).toString());
                }
                echo.setScore(score.get("总得分").toString());
                for (EchoPair echoPair : mapper.getEchoAndId(username, name)) {
                    int id = echoPair.getId();
                    Echo e = JSON.parseObject(echoPair.getEcho(), new TypeReference<Echo>() {});
                    boolean isEqual = e.getMain().equals(echo.getMain()) && e.getCost().equals(echo.getCost());
                    for (int i = 0; i < echo.getEcho().size(); i++) {
                        List<String> a = e.getEcho().get(i), b = echo.getEcho().get(i);
                        if (!(a.get(0).equals(b.get(0)) && a.get(1).equals(b.get(1)))) {
                            isEqual = false;
                            break;
                        }
                    }
                    if (isEqual) {
                        mapper.updateEcho(id, JSON.toJSONString(echo));
                    }
                }
            }
        }
        return data;
    }

    @Override
    public List<Map<String, String>> getEchoStats(String username) { // 获取声骸属性
        Map<String, List<Echo>> data = getData(username);
        List<Map<String, String>> res = new ArrayList<>(data.size());
        for (String name: data.keySet().stream().sorted(
                Comparator.comparing(c -> echoUtil.getPinyin(c))).toList()) {
            Map<String, Double> compute = new HashMap<>();
            for (String key: ECHO_KEYS) {
                compute.put(key, 0.0);
            }
            for (Echo echo: data.get(name)) {
                for (List<String> li: echo.getEcho()) {
                    if (li.get(0).isEmpty()) break;
                    compute.put(li.get(0), compute.get(li.get(0)) + Double.parseDouble(li.get(1)));
                }
            }
            Map<String, String> tmp = new LinkedHashMap<>();
            tmp.put("角色", name);
            for (Map.Entry<String, Double> entry: compute.entrySet()) {
                tmp.put(entry.getKey(), String.format("%.1f", entry.getValue()));
            }
            res.add(tmp);
        }
        return res;
    }

    @Override
    public int[] getEchoCnts(String username) { // 获取声骸总数
        Map<String, List<Echo>> data = getData(username);
        int[] res = new int[101];
        for (String name: data.keySet()) {
            for (Echo echo: data.get(name)) {
                if (echo.getScore().isEmpty()) break;
                res[Integer.parseInt(echo.getScore())]++;
            }
        }
        return res;
    }

    @Override
    public Map<String, Integer> getEchoRelativeDeviation(String username) { // 获取声骸标准化偏差
        Map<String, List<Echo>> data = getData(username);
        Map<String, Integer> res = new LinkedHashMap<>();
        Map<String, Double> sum = new LinkedHashMap<>();
        for (String key : ECHO_KEYS) {
            res.put(key, 0);
            sum.put(key, 0.0);
        }
        for (List<Echo> echoList: data.values()) {
            for (Echo echo: echoList) {
                if (echo.getScore().isEmpty()) break;
                for (List<String> li: echo.getEcho()) {
                    res.merge(li.get(0), 1, Integer::sum);
                    sum.merge(li.get(0), Double.parseDouble(li.get(1)), Double::sum);
                }
            }
        }
        for (String key : ECHO_KEYS) {
            if (sum.get(key) != 0) {
                res.put(key, (int) Math.round(sum.get(key) * 100 / res.get(key) / ECHO_AVERAGE.get(key)) - 100);
            }
        }
        return res;
    }

    @Override
    public Map<String, Integer> getEchoRelativeDeviation(String username, String name) { // 获取角色声骸标准化偏差
        Map<String, List<Echo>> data = getData(username);
        Map<String, Integer> res = new LinkedHashMap<>();
        Map<String, Double> sum = new LinkedHashMap<>();
        for (String key : ECHO_KEYS) {
            res.put(key, 0);
            sum.put(key, 0.0);
        }
        for (Echo echo: data.get(name)) {
            if (echo.getScore().isEmpty()) break;
            for (List<String> li: echo.getEcho()) {
                res.merge(li.get(0), 1, Integer::sum);
                sum.merge(li.get(0), Double.parseDouble(li.get(1)), Double::sum);
            }
        }
        for (String key : ECHO_KEYS) {
            if (sum.get(key) != 0) {
                res.put(key, (int) Math.round(sum.get(key) * 100 / res.get(key) / ECHO_AVERAGE.get(key)) - 100);
            }
        }
        return res;
    }

    @Override
    public Map<String, Double> getWeightKurtosis(String username) { // 获取权重过剩峰度
        Map<String, Double> res = new LinkedHashMap<>();
        for (String name : getNames()) {
            Map<String, ? extends Number> map = getweightsByUsername(username, name);
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (Number val : map.values()) {
                stats.addValue(val.doubleValue());
            }
            res.put(name, stats.getKurtosis());
        }
        return res;
    }

    @Override
    public void addTempEcho(String username, String echo, String nameList) { // 添加临时声骸
        mapper.insertTempEcho(username, echo, nameList);
    }

    @Override
    public List<Map<String, Echo>> getTempData(String username) { // 获取临时数据
        List<Map<String, Echo>> res = new ArrayList<>();
        List<EchoPair> echoPairs = mapper.getTempEchoList(username);
        for (EchoPair echoPair: echoPairs) {
            Map<String, Echo> item = new LinkedHashMap<>();
            List<Map.Entry<String, Echo>> tmpList = new ArrayList<>();
            Echo e = JSON.parseObject(echoPair.getEcho(), new TypeReference<Echo>() {});
            List<String> names = JSON.parseObject(echoPair.getNameList(), new TypeReference<List<String>>() {});
            Map<String, Number> values = new HashMap<>();
            for (List<String> li: e.getEcho()) {
                values.put(li.get(0), Double.parseDouble(li.get(1)));
            }
            String valuesJSON = JSON.toJSONString(values);
            for (String name: names) {
                Echo ee = Echo.getCloned(e);
                Map<String, Number> percent = getEchoPercent(username, name, valuesJSON);
                for (List<String> li: ee.getEcho()) {
                    li.set(2, percent.get(li.get(0)).toString());
                }
                ee.setScore(percent.get("总得分").toString());
                ee.getEcho().sort((a, b) -> Integer.parseInt(b.get(2)) - Integer.parseInt(a.get(2)));
                tmpList.add(Map.entry(name, ee));
            }
            double sum = 0, cnt = 0.0;
            for (Map.Entry<String, Echo> entry: tmpList) {
                Echo ee = entry.getValue();
                sum += Double.parseDouble(ee.getScore());
                cnt++;
            }
            item.put("声骸", e);
            tmpList.sort((a, b) -> Integer.parseInt(b.getValue().getScore()) - Integer.parseInt(a.getValue().getScore()));
            for (Map.Entry<String, Echo> entry: tmpList) {
                item.put(entry.getKey(), entry.getValue());
            }
            e.setScore(String.format("%.1f", sum / cnt));
            res.add(item);
        }
        res.sort((a, b) -> {
            if (a.size() == b.size()) {
                return (int) (100 * (Double.parseDouble(b.get("声骸").getScore()) - Double.parseDouble(a.get("声骸").getScore())));
            }
            return a.size() - b.size();
        });
        return res;
    }

    @Override
    public List<Map<String, Echo>> getTempDataByScreen(String username, String json) {
        Map<String, List<String>> check = JSON.parseObject(json, new TypeReference<Map<String, List<String>>>() {}); // 获取筛选后的临时数据
        Set<String> name = new HashSet<>(check.get("name"));
        Set<String> cost = new HashSet<>(check.get("cost"));
        Set<String> main = new HashSet<>(check.get("main"));
        Set<String> echo = new HashSet<>(check.get("echo"));
        int lower = Integer.parseInt(check.get("range").get(0)), upper = Integer.parseInt(check.get("range").get(1));
        List<Map<String, Echo>> moto = getTempData(username), res = new ArrayList<>();
        for (Map<String, Echo> stringEchoMap : moto) {
            Echo e = stringEchoMap.get("声骸");
            double score = Double.parseDouble(e.getScore());
            if (score < lower || score > upper || (!cost.isEmpty() && !cost.contains(e.getCost())) ||
                    (!main.isEmpty() && !main.contains(e.getMain().substring(0, e.getMain().indexOf(' '))))) {
                continue;
            }
            if (!echo.isEmpty()) {
                boolean found = false;
                for (List<String> li : e.getEcho()) {
                    if (echo.contains(li.get(0))) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    continue;
                }
            }
            Map<String, Echo> item = stringEchoMap;
            if (!name.isEmpty()) {
                item = new LinkedHashMap<>();
                item.put("声骸", stringEchoMap.get("声骸"));
                for (String key : check.get("name")) {
                    if (stringEchoMap.containsKey(key)) {
                        item.put(key, stringEchoMap.get(key));
                    }
                }
            }
            if (item.size() > 1) {
                res.add(item);
            }
        }
        return res;
    }

    @Override
    public void delTempSubEcho(String username, String json, String name) { // 移除临时声骸角色
        Map<String, Echo> map = JSON.parseObject(json, new TypeReference<Map<String, Echo>>() {});
        map.get("声骸").setScore("0");
        List<String> names = new ArrayList<>();
        for (String key: map.keySet()) {
            if (!key.equals("声骸")) names.add(key);
        }
        names.sort(String::compareTo);
        List<EchoPair> echoPairs = mapper.getTempEchoList(username);
        for (EchoPair echoPair: echoPairs) {
            Echo e = JSON.parseObject(echoPair.getEcho(), new TypeReference<Echo>() {});
            e.setScore("0");
            if (e.equals(map.get("声骸"))) {
                List<String> name_list = JSON.parseObject(echoPair.getNameList(), new TypeReference<List<String>>() {});
                name_list.sort(String::compareTo);
                if (name_list.equals(names)) {
                    names.remove(name);
                    if (names.isEmpty()) mapper.deleteTempEcho(echoPair.getId());
                    else mapper.updateTempEchoList(echoPair.getId(), JSON.toJSONString(names));
                    return;
                }
            }
        }
    }

    @Override
    public void delTempEcho(String username, String json) { // 移除临时声骸
        Map<String, Echo> map = JSON.parseObject(json, new TypeReference<Map<String, Echo>>() {});
        map.get("声骸").setScore("0");
        List<String> names = new ArrayList<>();
        for (String key: map.keySet()) {
            if (!key.equals("声骸")) names.add(key);
        }
        names.sort(String::compareTo);
        List<EchoPair> echoPairs = mapper.getTempEchoList(username);
        for (EchoPair echoPair: echoPairs) {
            Echo e = JSON.parseObject(echoPair.getEcho(), new TypeReference<Echo>() {});
            e.setScore("0");
            if (e.equals(map.get("声骸"))) {
                List<String> name_list = JSON.parseObject(echoPair.getNameList(), new TypeReference<List<String>>() {});
                name_list.sort(String::compareTo);
                if (name_list.equals(names)) {
                    mapper.deleteTempEcho(echoPair.getId());
                    return;
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchImportEcho(String username, List<RoleDTO> roles, Boolean isDelete) { // 批量导入声骸
        if (isDelete) {
            mapper.deleteTempEchoByUsername(username);
        }
        else {
            List<EchoPair> echoPairs = mapper.getEchoAndName(username);
            if ((echoPairs != null && !echoPairs.isEmpty())) {
                for (EchoPair echoPair : echoPairs) {
                    List<String> nameList = List.of(echoPair.getName());
                    echoPair.setNameList(JSON.toJSONString(nameList));
                    echoPair.setName(username);
                }
                mapper.batchInsertionTempEchos(echoPairs);
            }
        }
        mapper.deleteEchoByUsername(username);
        List<EchoDO> echos = new ArrayList<>();
        for (RoleDTO role: roles) {
            if (role == null) continue;
            String roleName = role.getRole().getRoleName();
            if (role.getRole().getIsMainRole()) {
                roleName = EchoConvert.mainRoleNameMap(role.getRole().getAttributeName());
            }
            List<RoleDTO.EquipPhantom> equipPhantomList = role.getPhantomData().getEquipPhantomList();
            if (equipPhantomList == null) continue;
            for (RoleDTO.EquipPhantom equipPhantom : equipPhantomList) {
                if (!EchoUtil.verifyEquipPhantom(equipPhantom)) {
                    continue;
                }
                Integer cost = equipPhantom.getCost();
                String main = equipPhantom.getMainProps().get(0).getAttributeName();
                main = EchoConvert.mainAttributeNameMap(main, cost);
                Map<String, Number> values = new HashMap<>();
                for (RoleDTO.EquipPhantomAttribute equipPhantomAttribute : equipPhantom.getSubProps()) {
                    String attributeName = equipPhantomAttribute.getAttributeName();
                    String attributeValue = equipPhantomAttribute.getAttributeValue();
                    attributeName = EchoConvert.subAttributeNameMap(attributeName, attributeValue);
                    attributeValue = EchoConvert.attributeValueMap(attributeValue);
                    values.put(attributeName, Double.parseDouble(attributeValue));
                }
                Map<String, Number> echoPercentMap = getEchoPercent(username, roleName, JSON.toJSONString(values));
                List<List<String>> echoList = new ArrayList<>();
                for (String attributeName : values.keySet()) {
                    String attributeValue = EchoConvert.attributeValueMap(
                            values.get(attributeName).toString()
                    );
                    String attributeScore = EchoConvert.attributeValueMap(
                            echoPercentMap.get(attributeName).toString()
                    );
                    echoList.add(Arrays.asList(attributeName, attributeValue, attributeScore));
                }
                echoList.sort(Comparator.comparing((List<String> item) ->
                                Double.parseDouble(EchoConvert.attributeValueMap(item.get(2))))
                        .reversed());
                Echo echo = new Echo(
                        main, cost.toString(), echoList,
                        EchoConvert.attributeValueMap(
                                echoPercentMap.get("总得分").toString()
                        ));
                echos.add(new EchoDO(username, roleName, JSON.toJSONString(echo)));
            }
        }
        if (!echos.isEmpty()) {
            mapper.batchInsertionEcho(echos);
        }
    }
}