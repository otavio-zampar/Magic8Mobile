package com.example.chatgpt;

import java.util.Random;

public class ChatGPT2 {

    public static String realPromptBelieveIt(){
        String[] str = {
                "A resposta está nebulosa, tente novamente.",
                "Pergunte novamente mais tarde.",
                "Melhor não te dizer agora.",
                "Não é possível prever agora.",
                "Concentre-se e pergunte novamente.",
                "Não conte com isso.",
                "Minha resposta é não.",
                "Minhas fontes dizem não.",
                "As perspectivas não são boas.",
                "Muito improvável.",
                "Está em suas mãos.",
                "A resposta está dentro de você.",
                "Você já conhece a resposta.",
                "Confie em seus instintos.",
                "Busque orientação de dentro de si.",
                "Dê um salto de fé.",
                "Abraçar a incerteza.",
                "Explore novas possibilidades.",
                "Acredite em si mesmo.",
                "Encontre equilíbrio em suas decisões.",
                "Tenha paciência e espere pela clareza.",
                "Busque conselhos de uma fonte confiável.",
                "Reflita profundamente sobre sua pergunta.",
                "Siga o desejo do seu coração.",
                "Mantenha a mente aberta.",
                "Confie no processo.",
                "Tome medidas e veja o que se desenrola.",
                "Sim.",
                "Não.",
                "Talvez.",
                "Certamente.",
                "Isso parece improvável.",
                "Pode ser uma possibilidade.",
                "Não conte com isso.",
                "Não vejo isso acontecendo.",
                "Os sinais apontam para sim.",
                "Está tudo indicando não.",
                "É uma incerteza.",
                "Pode ser, mas não tenho certeza.",
                "Talvez, se as circunstâncias forem favoráveis.",
                "Não posso prever agora.",
                "Provavelmente sim.",
                "Provavelmente não.",
                "Não estou convencido.",
                "Não apostaria nisso.",
                "Depende de vários fatores.",
                "Não consigo responder no momento.",
                "Essa é uma pergunta difícil.",
                "Os resultados não são claros.",
                "A resposta está além do meu alcance.",
                "Só o tempo dirá.",
                "Você precisa tomar uma decisão.",
                "Pense bem antes de prosseguir.",
                "Considere todas as opções antes de decidir.",
                "É melhor buscar mais informações.",
                "Confie em sua intuição.",
                "Consulte alguém de confiança.",
                "Faça uma análise mais profunda.",
                "Pondere as possibilidades.",
                "Não há garantias.",
                "Não se precipite.",
                "Busque mais clareza antes de decidir.",
                "A resposta está dentro de você.",
                "Siga sua intuição.",
                "Leve em consideração suas emoções.",
                "Confie em si mesmo.",
                "Esteja aberto a novas perspectivas.",
                "Não tenha medo de arriscar.",
                "Seja flexível em sua abordagem.",
                "Não se preocupe, tudo se resolverá.",
                "Mantenha uma atitude positiva.",
                "Acredite em suas habilidades.",
                "Não deixe o medo te paralisar."
        };
        Random random = new Random();
        return str[random.nextInt(str.length)];
    }

    public static String getVoice(){
        String[] str = {
                "pt-br-x-afs#female_1-local",
                "pt-br-x-afs#female_2-local",
                "pt-br-x-afs#female_3-local",
                "pt-br-x-afs#male_3-local"
        };
        Random random = new Random();
        return str[random.nextInt(str.length)];
    }



//    public static String Prompt(String text) throws Exception {
//        String url = "https://api.openai.com/v1/completions";
//        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
//
//        con.setRequestMethod("POST");
//        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("Authorization", "Bearer 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");
//
//        JSONObject data = new JSONObject();
//        data.put("model", "gpt-3.5-turbo");
//        data.put("prompt", text);
//        data.put("max_tokens", 7);
//        data.put("temperature", 1.0);
//
//        con.setDoOutput(true);
//        con.getOutputStream().write(data.toString().getBytes());
//
//        String output = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
//                    .reduce((a, b) -> a + b).get();
//        }
//
//        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
//    }
}
