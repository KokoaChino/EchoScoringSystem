package com.echo.controller;

import com.common.constant.Constant;
import com.common.context.UserContext;
import com.echo.dto.RoleDTO;
import com.echo.entity.Echo;
import com.echo.entity.Character;
import com.echo.entity.Weapon;
import com.echo.external.service.api.CharacterExtraConfigService;
import com.echo.external.service.api.GameDataCacheService;
import com.echo.service.api.EchoScoringSystemService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/echo")
public class EchoScoringSystemController {

    @Resource
    private EchoScoringSystemService service;

    @Resource
    private CharacterExtraConfigService configService;

    @Resource
    private GameDataCacheService gameDataCacheService;

    @GetMapping("/get-echo-keys")
    public String[] getEchoKeys() { // 获取副词条名称
        return service.getEchoKeys();
    }

    @GetMapping("/get-echo-values")
    public Map<String, double[]> getEchoValues() { // 获取副词条取值
        return service.getEchoValues();
    }

    @GetMapping("/get-echo-average")
    public Map<String, Double> getEchoAverage() { // 获取副词条基准值
        return service.getEchoAverage();
    }

    @GetMapping("/get-weapons")
    public Map<String, Weapon> getWeapons() { // 获取武器
        return service.getWeapons();
    }

    @PostMapping("/get-weapons-by-screen")
    public List<Weapon> getWeaponsByScreen(@RequestParam("json") String json) { // 获取筛选后的武器
        return service.getWeaponsByScreen(json);
    }

    @PostMapping("/set-weapon")
    public void setWeapon(
            @RequestParam("name") String name,
            @RequestParam("weapon") String weapon) {
        String username = UserContext.getUsername();
        service.setWeapon(username, name, weapon);
    }

    @PostMapping("/get-characters")
    public Map<String, Character> getCharacters() { // 获取角色
        String username = UserContext.getUsername();
        return service.getCharacters(username);
    }

    @PostMapping("/get-characters-by-screen")
    public Map<String, Character> getCharactersByScreen(
            @RequestParam("names") String json) {
        String username = UserContext.getUsername();
        return service.getCharactersByScreen(username, json);
    }

    @GetMapping("/get-names")
    public List<String> getNames() { // 获取角色名称列表
        return service.getNames();
    }

    @GetMapping("/get-character-groups")
    public Map<String, List<String>> getCharacterGroupsByType() { // 获取角色分组
        return service.getCharacterGroupsByType();
    }

    @GetMapping("/get-character-stats")
    public Map<String, int[]> getCharacterStats() { // 获取角色三维属性
        return service.getCharacterStats();
    }

    @PostMapping("/get-weights")
    public Map<String, ? extends Number> getWeightsByUsername(
            @RequestParam("name") String name) { // 获取角色完整副词条权重
        String username = UserContext.getUsername();
        return service.getweightsByUsername(username, name);
    }

    @GetMapping("/get-all-weights")
    public Map<String, Map<String, ? extends Number>> getAllWeightsByUsername() { // 获取所有角色完整副词条权重
        String username = UserContext.getUsername();
        return service.getAllweightsByUsername(username);
    }

    @PostMapping("/set-weights")
    public void setWeights(
            @RequestParam("name") String name,
            @RequestParam("json") String json) { // 设置角色副词条权重
        String username = UserContext.getUsername();
        service.setweights(username, name, json);
    }

    @PostMapping("/re-weights")
    public void reWeights(@RequestParam("name") String name) { // 重置角色副词条权重
        String username = UserContext.getUsername();
        service.reweights(username, name);
    }

    @PostMapping("/get-echo-percent")
    public Map<String, Number> getEchoPercent(
            @RequestParam("name") String name,
            @RequestParam("values") String json) { // 获取声骸评分
        String username = UserContext.getUsername();
        return service.getEchoPercent(username, name, json);
    }

    @PostMapping("/get-data")
    private Map<String, List<Echo>> getData() { // 获取角色词条数据
        String username = UserContext.getUsername();
        return service.getData(username);
    }

    @PostMapping("/get-data-by-screen")
    private Map<String, List<Echo>> getDataByScreen(@RequestParam("json") String json) { // 获取筛选后的角色词条数据
        String username = UserContext.getUsername();
        return service.getDataByScreen(username, json);
    }

    @PostMapping("/add-data")
    public Map<String, List<Echo>> addData(
            @RequestParam("json") String json,
            @RequestParam("name") String name) { // 添加角色声骸数据
        String username = UserContext.getUsername();
        return service.addData(username, json, name);
    }

    @PostMapping("/del-echo")
    public Map<String, List<Echo>> delEcho(
            @RequestParam("index") Integer index,
            @RequestParam("name") String name) { // 删除角色声骸数据
        String username = UserContext.getUsername();
        return service.delEcho(username, index, name);
    }

    @PostMapping("/re-data")
    public Map<String, List<Echo>> reData() { // 重新计算角色声骸数据
        String username = UserContext.getUsername();
        return service.reData(username);
    }

    @PostMapping("/get-echo-stats")
    public List<Map<String, String>> getEchoStats() { // 获取声骸属性
        String username = UserContext.getUsername();
        return service.getEchoStats(username);
    }

    @PostMapping("/get-echo-cnts")
    public int[] getEchoCnts() { // 获取声骸总数
        String username = UserContext.getUsername();
        return service.getEchoCnts(username);
    }

    @PostMapping("/get-echo-relative-deviation")
    public Map<String, Integer> getEchoRelativeDeviation() { // 获取声骸标准化偏差
        String username = UserContext.getUsername();
        return service.getEchoRelativeDeviation(username);
    }

    @PostMapping("/get-echo-relative-deviation-by-name")
    public Map<String, Integer> getEchoRelativeDeviation(@RequestParam("name") String name) { // 获取角色声骸标准化偏差
        String username = UserContext.getUsername();
        return service.getEchoRelativeDeviation(username, name);
    }

    @PostMapping("/get-weight-kurtosis")
    public Map<String, Double> getWeightKurtosis() { // 获取权重过剩峰度
        String username = UserContext.getUsername();
        return service.getWeightKurtosis(username);
    }

    @PostMapping("/add-temp-echo")
    public void addTempEcho(
            @RequestParam("echo") String echo,
            @RequestParam("name_list") String nameList) { // 添加临时声骸
        String username = UserContext.getUsername();
        service.addTempEcho(username, echo, nameList);
    }

    @PostMapping("/get-temp-data-by-screen")
    public List<Map<String, Echo>> getTempDataByScreen(@RequestParam("json") String json) {
        String username = UserContext.getUsername();
        return service.getTempDataByScreen(username, json);
    }

    @PostMapping("/del-temp-sub-echo")
    public void delTempSubEcho(
            @RequestParam("json") String json,
            @RequestParam("name") String name) { // 移除临时声骸角色
        String username = UserContext.getUsername();
        service.delTempSubEcho(username, json, name);
    }

    @PostMapping("/del-temp-echo")
    public void delTempEcho(@RequestParam("json") String json) { // 移除临时声骸
        String username = UserContext.getUsername();
        service.delTempEcho(username, json);
    }

    @PostMapping("/batch-import-echo")
    public void batchImportEcho(
            @RequestBody List<RoleDTO> roles,
            @RequestParam("isDelete") Boolean isDelete) { // 批量导入声骸
        String username = UserContext.getUsername();
        service.batchImportEcho(username, roles, isDelete);
    }

    @GetMapping("/refresh-character-config/{secret}/{source}")
    public String refresh(@PathVariable("secret") String secret, @PathVariable("source") String source) {
        if (!secret.equals(Constant.SECRET)) {
            return "校验失败";
        }
        if (source.equals("data")) {
            gameDataCacheService.refreshCache();
        } else if (source.equals("config")) {
            configService.reload();
        } else {
            return "校验失败";
        }
        return "OK";
    }
}