package com.example.controller;

import java.util.*;
import com.example.entity.echo.Echo;
import com.example.service.api.EchoScoringSystemService;
import com.example.entity.echo.Weapon;
import com.example.entity.echo.Character;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/echo-scoring-system")
public class EchoScoringSystemController { // 声骸评分系统控制器

    @Resource
    EchoScoringSystemService service;

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
    public void setWeapon(@RequestParam("username") String username,
                          @RequestParam("name") String name,
                          @RequestParam("weapon") String weapon) { // 设置角色武器
        service.setWeapon(username, name, weapon);
    }

    @PostMapping("/get-characters")
    public Map<String, Character> getCharacters(@RequestBody String username) { // 获取角色
        username = username.substring(1, username.length() - 1);
        return service.getCharacters(username);
    }

    @PostMapping("/get-characters-by-screen")
    public Map<String, Character> getCharactersByScreen(@RequestParam("username") String username,
                                                        @RequestParam("names") String json) { // 获取筛选后的角色
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

    @PostMapping("/get-weigths")
    public Map<String, ? extends Number> getWeigthsByUsername(@RequestParam("username") String username,
                                                              @RequestParam("name") String name) { // 获取角色完整副词条权重
        return service.getWeigthsByUsername(username, name);
    }

    @PostMapping("/set-weights")
    public void setWeigths(@RequestParam("username") String username,
                           @RequestParam("name") String name,
                           @RequestParam("json") String json) { // 设置角色副词条权重
        service.setWeigths(username, name, json);
    }

    @PostMapping("/re_weights")
    public void reWeigths(@RequestParam("username") String username,
                          @RequestParam("name") String name) { // 重置角色副词条权重
        service.reWeigths(username, name);
    }

    @PostMapping("/get-echo-percent")
    public Map<String, Number> getEchoPercent(@RequestParam("username") String username,
                                              @RequestParam("name") String name,
                                              @RequestParam("values") String json) { // 获取声骸评分
        return service.getEchoPercent(username, name, json);
    }

    @PostMapping("/get-data")
    private Map<String, List<Echo>> getData(@RequestBody String username) { // 获取角色词条数据
        username = username.substring(1, username.length() - 1);
        return service.getData(username);
    }

    @PostMapping("/get-data-by-screen")
    private Map<String, List<Echo>> getDataByScreen(@RequestParam("username") String username,
                                                    @RequestParam("json") String json) { // 获取筛选后的角色词条数据
        return service.getDataByScreen(username, json);
    }

    @PostMapping("/add-data")
    public Map<String, List<Echo>> addData(@RequestParam("username") String username,
                                           @RequestParam("json") String json,
                                           @RequestParam("name") String name) { // 添加角色声骸数据
        return service.addData(username, json, name);
    }

    @PostMapping("/del-echo")
    public Map<String, List<Echo>> delEcho(@RequestParam("username") String username,
                                           @RequestParam("index") Integer index,
                                           @RequestParam("name") String name) { // 删除角色声骸数据
        return service.delEcho(username, index, name);
    }

    @PostMapping("/re-data")
    public Map<String, List<Echo>> reData(@RequestBody String username) { // 重新计算角色声骸数据
        username = username.substring(1, username.length() - 1);
        return service.reData(username);
    }

    @PostMapping("/get-echo-stats")
    public List<Map<String, String>> getEchoStats(@RequestBody String username) { // 获取声骸属性
        username = username.substring(1, username.length() - 1);
        return service.getEchoStats(username);
    }

    @PostMapping("/get-echo-cnts")
    public int[] getEchoCnts(@RequestBody String username) { // 获取声骸总数
        username = username.substring(1, username.length() - 1);
        return service.getEchoCnts(username);
    }

    @PostMapping("/get-echo-relative-deviation")
    public Map<String, Integer> getEchoRelativeDeviation(@RequestBody String username) { // 获取声骸标准化偏差
        username = username.substring(1, username.length() - 1);
        return service.getEchoRelativeDeviation(username);
    }

    @PostMapping("/get-echo-relative-deviation-by-name")
    public Map<String, Integer> getEchoRelativeDeviation(@RequestParam("username") String username,
                                                         @RequestParam("name") String name) { // 获取角色声骸标准化偏差
        return service.getEchoRelativeDeviation(username, name);
    }

    @PostMapping("/get-weight-kurtosis")
    public Map<String, Double> getWeightKurtosis(@RequestBody String username) { // 获取权重过剩峰度
        username = username.substring(1, username.length() - 1);
        return service.getWeightKurtosis(username);
    }

    @PostMapping("/add-temp-echo")
    public void addTempEcho(@RequestParam("username") String username,
                            @RequestParam("echo") String echo,
                            @RequestParam("name_list") String nameList) { // 添加临时声骸
        service.addTempEcho(username, echo, nameList);
    }

    @PostMapping("/get-temp-data")
    public List<Map<String, Echo>> getTempData(@RequestParam("username") String username) { // 获取临时数据
        return service.getTempData(username);
    }

    @PostMapping("/get-temp-data-by-screen")
    public List<Map<String, Echo>> getTempDataByScreen(@RequestParam("username") String username,
                                                   @RequestParam("json") String json) {
        return service.getTempDataByScreen(username, json);
    }

    @PostMapping("/del-temp-sub-echo")
    public void delTempSubEcho(@RequestParam("username") String username,
                               @RequestParam("json") String json,
                               @RequestParam("name") String name) { // 移除临时声骸角色
        service.delTempSubEcho(username, json, name);
    }

    @PostMapping("/del-temp-echo")
    public void delTempEcho(@RequestParam("username") String username,
                            @RequestParam("json") String json) { // 移除临时声骸
        service.delTempEcho(username, json);
    }
}