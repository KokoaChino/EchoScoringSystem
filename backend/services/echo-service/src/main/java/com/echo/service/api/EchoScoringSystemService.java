package com.echo.service.api;

import com.echo.dto.RoleDTO;
import com.echo.entity.Echo;
import com.echo.entity.Weapon;
import com.echo.entity.Character;
import java.util.List;
import java.util.Map;


public interface EchoScoringSystemService {

    String[] getEchoKeys(); // 获取副词条名称
    Map<String, double[]> getEchoValues(); // 获取副词条取值
    Map<String, Double> getEchoAverage(); // 获取副词条基准值

    Map<String, Weapon> getWeapons(); // 获取武器
    List<Weapon> getWeaponsByScreen(String json); // 获取筛选后的武器
    void setWeapon(String username, String name, String weapon); // 设置角色武器

    Map<String, Character> getCharacters(String username); // 获取角色
    Map<String, Character> getCharactersByScreen(String username, String json); // 获取筛选后的角色
    List<String> getNames(); // 获取角色名称列表
    Map<String, List<String>> getCharacterGroupsByType(); // 获取角色分组
    Map<String, int[]> getCharacterStats(); // 获取角色三维属性
    Map<String, ? extends Number> getweightsByUsername(String username, String name); // 获取角色完整副词条权重
    Map<String, Map<String, ? extends Number>> getAllweightsByUsername(String username); // 获取所有角色完整副词条权重
    void setweights(String username, String name, String json); // 设置角色副词条权重
    void reweights(String username, String name); // 重置角色副词条权重

    Map<String, Number> getEchoPercent(String username, String name, String json); // 获取声骸评分

    Map<String, List<Echo>> getData(String username); // 获取角色词条数据
    Map<String, List<Echo>> getDataByScreen(String username, String json); // 获取筛选后的角色词条数据
    Map<String, List<Echo>> addData(String username, String json, String name); // 添加角色声骸数据
    Map<String, List<Echo>> delEcho(String username, Integer index, String name); // 删除角色声骸数据
    Map<String, List<Echo>> reData(String username); // 重新计算角色声骸数据
    void reData(); // 重新计算所有声骸数据
    List<Map<String, String>> getEchoStats(String username); // 获取声骸属性

    int[] getEchoCnts(String username); // 获取声骸总数
    Map<String, Integer> getEchoRelativeDeviation(String username); // 获取声骸标准化偏差
    Map<String, Integer> getEchoRelativeDeviation(String username, String name); // 获取角色声骸标准化偏差
    Map<String, Double> getWeightKurtosis(String username); // 获取权重过剩峰度

    void addTempEcho(String username, String echo, String nameList); // 添加临时声骸
    List<Map<String, Echo>> getTempData(String username); // 获取临时数据
    List<Map<String, Echo>> getTempDataByScreen(String username, String json);
    void delTempSubEcho(String username, String json, String name); // 移除临时声骸角色
    void delTempEcho(String username, String json); // 移除临时声骸

    void batchImportEcho(String username, List<RoleDTO> roles, Boolean isDelete); // 批量导入声骸
}