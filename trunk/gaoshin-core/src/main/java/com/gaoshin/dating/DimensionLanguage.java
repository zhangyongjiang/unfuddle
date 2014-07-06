package com.gaoshin.dating;

public enum DimensionLanguage {
    Afrikaans("Afrikaans", 10),
    Albanian("Albanian", 20),
    AncientGreek("Ancient Greek", 30),
    Arabic("Arabic", 40),
    Basque("Basque", 50),
    Belarusan("Belarusan", 60),
    Bengali("Bengali", 70),
    Breton("Breton", 80),
    Bulgarian("Bulgarian", 90),
    Catalan("Catalan", 100),
    Cebuano("Cebuano", 110),
    Chechen("Chechen", 120),
    ChineseMandarin("Chinese(Mandarin)", 130),
    ChineseCantonese("Chinese(Cantonese)", 140),
    Croatian("Croatian", 150),
    Czech("Czech", 160),
    Danish("Danish", 170),
    Dutch("Dutch", 180),
    English("English", 190),
    Esperanto("Esperanto", 200),
    Estonian("Estonian", 210),
    Farsi("Farsi", 220),
    Finnish("Finnish", 230),
    French("French", 240),
    Frisian("Frisian", 250),
    Georgian("Georgian", 260),
    German("German", 270),
    Greek("Greek", 280),
    Hawaiian("Hawaiian", 290),
    Hebrew("Hebrew", 300),
    Hindi("Hindi", 310),
    Hungarian("Hungarian", 320),
    Icelandic("Icelandic", 330),
    Ilongo("Ilongo", 340),
    Indonesian("Indonesian", 350),
    Irish("Irish", 360),
    Italian("Italian", 370),
    Japanese("Japanese", 380),
    Khmer("Khmer", 390),
    Korean("Korean", 400),
    Latin("Latin", 410),
    Latvian("Latvian", 420),
    Lisp("Lisp", 430),
    Lithuanian("Lithuanian", 440),
    Malay("Malay", 450),
    Maori("Maori", 460),
    Mongolian("Mongolian", 470),
    Norwegian("Norwegian", 480),
    Occitan("Occitan", 490),
    Other("Other", 500),
    Persian("Persian", 510),
    Polish("Polish", 520),
    Portuguese("Portuguese", 530),
    Romanian("Romanian", 540),
    Rotuman("Rotuman", 550),
    Russian("Russian", 560),
    Sanskrit("Sanskrit", 570),
    Sardinian("Sardinian", 580),
    Serbian("Serbian", 590),
    SignLanguage("Sign language", 600),
    Slovak("Slovak", 610),
    Slovenian("Slovenian", 620),
    Spanish("Spanish", 630),
    Swahili("Swahili", 640),
    Swedish("Swedish", 650),
    Tagalog("Tagalog", 660),
    Tamil("Tamil", 670),
    Thai("Thai", 680),
    Tibetan("Tibetan", 690),
    Turkish("Turkish", 700),
    Ukrainian("Ukrainian", 710),
    Urdu("Urdu", 720),
    Vietnamese("Vietnamese", 730),
    Welsh("Welsh", 740),
    Yiddish("Yiddish", 750), ;

    private String label;
    private int code;

    private DimensionLanguage(String label, int code) {
        this.label = label;
        this.code = code;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
