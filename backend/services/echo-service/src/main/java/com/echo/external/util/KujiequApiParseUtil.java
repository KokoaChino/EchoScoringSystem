package com.echo.external.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class KujiequApiParseUtil {

    public static String extractTextByLabel(String html, String label) { // 从基础信息 HTML 中提取指定标签对应的值
        Document doc = Jsoup.parse(html);
        for (Element tr : doc.select("tr")) {
            Elements tds = tr.select("td");
            if (tds.size() >= 2) {
                String labelText = tds.get(0).text().trim();
                String valueText = tds.get(1).text().trim();
                if (labelText.equals(label) || labelText.contains(label)) {
                    return valueText;
                }
            }
        }
        throw new RuntimeException("未找到标签：" + label);
    }

    public static String extractFirstImageUrl(String html) { // 提取武器图片 URL
        Document doc = Jsoup.parse(html);
        Element img = doc.select("tr:first-of-type img").first();
        if (img != null && img.hasAttr("src")) {
            return img.absUrl("src");
        }
        throw new RuntimeException("未找到武器图片");
    }

    public static int extractAtkFromHtml(String html) { // 从 90 级面板 HTML 中提取攻击力
        Document doc = Jsoup.parse(html);
        for (Element p : doc.select("p")) {
            String text = p.text().trim();
            if (text.contains("攻击力")) {
                String[] parts = text.split("[:：]");
                if (parts.length >= 2) {
                    return Integer.parseInt(parts[1].replaceAll("[,\\s]", ""));
                }
            }
        }
        throw new RuntimeException("未找到攻击力字段");
    }

    public static Integer extractStatFromTable(String html, String statName) { // 从面板表格 HTML 中提取某行的数值
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("tbody tr");
        for (Element row : rows) {
            Elements tds = row.select("td");
            if (tds.size() < 2) continue;
            String firstCellText = tds.get(0).text().trim();
            if (firstCellText.contains(statName)) {
                Element valueCell = tds.size() >= 2 ? tds.get(1) : null;
                if (valueCell != null) {
                    return Integer.parseInt(valueCell.text().replaceAll("[,\\s]", ""));
                }
            }
        }
        throw new RuntimeException("未找到基础三维字段");
    }
}