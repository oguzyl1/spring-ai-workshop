package dev.oguzhan.workshop.tools.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeatherTools {

    private static final String BASE_URL = "https://api.weather.gov";
    private final RestClient restClient;

    /*
     * RestClient, weather.gov API'sine yapılacak HTTP çağrıları için
     * Spring Boot tarafından sağlanan builder üzerinden oluşturulur.
     */
    public WeatherTools(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(BASE_URL)
                .defaultHeader("Accept", "application/geo+json")
                .defaultHeader("User-Agent", "SpringAIWeatherClient/1.0 (your@email.com)")
                .build();
    }

    /*
     * /points endpointinden dönen response içerisinden yalnızca forecast
     * URL'sine ihtiyaç duyduğumuz için ilgili alanlar map edilir.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Points(@JsonProperty("properties") Props properties) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Props(@JsonProperty("forecast") String forecast) {
        }
    }

    /*
     * Weather forecast response içerisindeki tahmin periyotlarını
     * temsil eden veri yapıları.
     *
     * Kullanmadığımız JSON alanları ignore edilir.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Forecast(@JsonProperty("properties") Props properties) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Props(@JsonProperty("periods") List<Period> periods) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Period(
                @JsonProperty("number") Integer number,
                @JsonProperty("name") String name,
                @JsonProperty("startTime") String startTime,
                @JsonProperty("endTime") String endTime,
                @JsonProperty("isDaytime") Boolean isDaytime,
                @JsonProperty("temperature") Integer temperature,
                @JsonProperty("temperatureUnit") String temperatureUnit,
                @JsonProperty("temperatureTrend") String temperatureTrend,
                @JsonProperty("probabilityOfPrecipitation") Map<?, ?> probabilityOfPrecipitation,
                @JsonProperty("windSpeed") String windSpeed,
                @JsonProperty("windDirection") String windDirection,
                @JsonProperty("icon") String icon,
                @JsonProperty("shortForecast") String shortForecast,
                @JsonProperty("detailedForecast") String detailedForecast) {
        }
    }


    /*
     * Aktif hava durumu uyarılarının API response yapısını temsil eder.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Alert(@JsonProperty("features") List<Feature> features) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Feature(@JsonProperty("properties") Properties properties) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Properties(
                @JsonProperty("event") String event,
                @JsonProperty("areaDesc") String areaDesc,
                @JsonProperty("severity") String severity,
                @JsonProperty("description") String description,
                @JsonProperty("instruction") String instruction) {
        }
    }


    /*
     * Model, kullanıcının belirli bir koordinat için hava tahmini
     * istediğini anladığında bu tool'u çağırabilir.
     */
    @Tool(description = "Belirli bir enlem ve boylam için güncel hava durumu tahminini getirir.")
    public String getWeatherForecastByLocation(
            @ToolParam(description = "Konumun enlem değeri") double latitude,
            @ToolParam(description = "Konumun boylam değeri") double longitude) {

        /*
         * weather.gov API'sinde önce koordinata ait forecast endpointini
         * öğrenmemiz gerekir.
         */
        Points points = restClient.get()
                .uri("/points/{latitude},{longitude}", latitude, longitude)
                .retrieve()
                .body(Points.class);

        /*
         * İlk request'ten elde edilen forecast URL'si kullanılarak
         * gerçek hava tahmini alınır.
         */
        Forecast forecast = restClient.get()
                .uri(points.properties().forecast())
                .retrieve()
                .body(Forecast.class);

        return forecast.properties()
                .periods()
                .stream()
                .map(period -> String.format("""
                                %s:
                                Sıcaklık: %s %s
                                Rüzgar: %s %s
                                Tahmin: %s
                                """,
                        period.name(),
                        period.temperature(),
                        period.temperatureUnit(),
                        period.windSpeed(),
                        period.windDirection(),
                        period.detailedForecast()
                ))
                .collect(Collectors.joining("\n"));
    }

    /*
     * Kullanıcı belirli bir ABD eyaletindeki aktif hava durumu
     * uyarılarını sorduğunda model bu tool'u çağırabilir.
     */
    @Tool(description = "Belirli bir ABD eyaletindeki aktif hava durumu uyarılarını getirir.")
    public String getAlerts(@ToolParam(description = "İki harfli ABD eyalet kodu. Örneğin CA veya NY.") String state) {
        Alert alert = restClient.get()
                .uri("/alerts/active/area/{state}", state)
                .retrieve()
                .body(Alert.class);

        return alert.features()
                .stream()
                .map(feature -> String.format("""
                                Olay: %s
                                Bölge: %s
                                Şiddet: %s
                                Açıklama: %s
                                Talimatlar: %s
                                """,
                        feature.properties().event(),
                        feature.properties().areaDesc(),
                        feature.properties().severity(),
                        feature.properties().description(),
                        feature.properties().instruction()
                ))
                .collect(Collectors.joining("\n"));
    }
}