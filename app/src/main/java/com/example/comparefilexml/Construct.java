package com.example.comparefilexml;

import java.util.List;

public class Construct {

    public static String[] attributeCancel = new String[]{
            "time",
            "hash",
            "h",
            "t"
    };

    public String[] section = new String[]{
            "noteTree", "contentFileList", "attributes", "pageList", "floatingObjectList", "bodyText"
    };
    public String[] tagList = new String[]{
            "page",
            //"layerList",
            "layer",
            "objectList",
            "object"
    };

    public String[] attribute = new String[]{
            "id",
            "type",
            "penSize",
            "color",
            "sessionHistory"
    };

    public static String r = "Hồng đậu nở, nở tương tư, không người ngắm\u200B\n" +
            "Bờ dương liễu, sớm trăng tàn, muộn gió trường đình\u200B\n" +
            "Ở cố hương, tường hồng giấu vần thơ khắc dung mạo người xưa\u200B\n" +
            "Chôn hồn hoa, ở đâu bên bờ Tiêu Tương ngắm hoa rơi";


    public static String t = "";

    public static String com = "";
}

//pageList

//page          id
//------------------------------------layerList
//layer         id
//------------------------------------objectList
//object        type    id
//
//
//
//get getAttributes id
//  -> not null ( *** -> getAttributes type -> not null)
//  sort by id
//
//

