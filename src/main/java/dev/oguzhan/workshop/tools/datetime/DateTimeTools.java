package dev.oguzhan.workshop.tools.datetime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

public class DateTimeTools {

    /*
     * @Tool annotation'ı bu Java metodunu LLM tarafından çağrılabilir
     * bir tool haline getirir.
     *
     * Description, modelin bu tool'un ne yaptığını ve hangi durumda
     * kullanılması gerektiğini anlamasına yardımcı olur.
     */
    @Tool(description = "Kullanıcının saat dilimindeki geçerli tarih ve saati getir")
    public String getCurrentTime(){

        /*
         * LocaleContextHolder üzerinden mevcut request'in timezone bilgisi
         * alınır ve tarih/saat bu timezone ile birlikte döndürülür.
         */
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();

    }
}
