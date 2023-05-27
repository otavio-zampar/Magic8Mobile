package com.example.chatgpt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.JSONObject;

public class ChatGPT2 {

    public static String realPromptBelieveIt(){
        String str[] = {
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
                "Esteja preparado para resultados inesperados.",
                "Deixe de lado dúvidas e medos.",
                "Mantenha-se positivo e otimista.",
                "Confie na jornada, não apenas no destino.",
                "Ouça sua voz interior.",
                "Aceite a resposta, seja ela esperada ou não.",
                "Abraçe o mistério do desenrolar da vida.",
                "Confie em si mesmo e siga em frente.",
                "Deixe o passado para trás e abrace o futuro.",
                "Busque a harmonia em todas as áreas da sua vida.",
                "Persevere, mesmo diante dos desafios.",
                "O amor está ao seu redor, abra os olhos para enxergá-lo.",
                "Esteja aberto a novas oportunidades e experiências.",
                "Acredite nas suas habilidades e conquiste seus objetivos.",
                "O universo conspira a seu favor, confie.",
                "O tempo trará as respostas que você procura.",
                "Aproveite o presente e viva um dia de cada vez.",
                "Seja gentil e generoso com os outros.",
                "Não tema as mudanças, elas trazem crescimento.",
                "Concentre-se no que é realmente importante para você.",
                "A gratidão é a chave para a felicidade.",
                "Deixe sua intuição guiar seus passos.",
                "Aprenda com os erros e siga em frente com sabedoria.",
                "Cultive relacionamentos saudáveis e significativos.",
                "Seja corajoso e abrace o desconhecido.",
                "A vida é uma jornada de autodescoberta.",
                "Confie no processo e aceite o fluxo da vida.",
                "Acredite no seu potencial ilimitado."
        };
        Random random = new Random();
        return str[random.nextInt(str.length)];
//        return String.valueOf(str.length);
    }
    public static String Prompt(String text) throws Exception {
        String url = "https://api.openai.com/v1/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer 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");

        JSONObject data = new JSONObject();
        data.put("model", "gpt-3.5-turbo");
        data.put("prompt", text);
        data.put("max_tokens", 7);
        data.put("temperature", 1.0);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                    .reduce((a, b) -> a + b).get();
        }

        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
    }
}
