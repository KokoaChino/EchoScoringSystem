package com.example.entity.echo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
@AllArgsConstructor
public class Echo { // 声骸

    public String main;
    public String cost;
    public List<List<String>> echo;
    public String score;

    public static Echo getEmpty() {
        List<List<String>> echo = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<String> tmp = new ArrayList<>(Arrays.asList("", "", ""));
            echo.add(tmp);
        }
        return new Echo("", "", echo, "");
    }

    public static Echo getCloned(Echo echo) {
        Echo res = getEmpty();
        res.main = echo.main;
        res.cost = echo.cost;
        res.score = echo.score;
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<String> tmp = new ArrayList<>(echo.echo.get(i));
            list.add(tmp);
        }
        res.echo = list;
        return res;
    }
}