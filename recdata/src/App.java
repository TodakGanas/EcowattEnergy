import java.time.OffsetDateTime;
import java.util.List;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

public class App {
    public static void main(String[] args) throws Exception {
        char[] token = "PAd4rNjKgnkVhUU1OlAsKW7jkyb7ftwCfIHcUEvESCnr8raRcS6ZODiAlasAkdKYGpGj2QWrPtFfxETsB7m93w==".toCharArray();  //le jeton
        String org = "zhar";     //le nom d'organization
        String bucket = "ecowatt";  //le nom de bucket ou la base de données
    
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://34.163.151.230:8086/", token, org, bucket);  //on specifie l'url de notre serveur
    
        WriteApiBlocking writeApi_jour = influxDBClient.getWriteApiBlocking();
        //recuperation de donnees depuis influxDB
        OffsetDateTime now = OffsetDateTime.now(); 
        OffsetDateTime end = OffsetDateTime.now().plusDays(1);

        String start = now.toString();
        String finish = end.toString();

        start = start.substring(0,11) + "00:00:00.000" + start.substring(23);
        finish = finish.substring(0,11) + "00:00:00.000" + finish.substring(23);

        String query = "from(bucket: \"ecowatt\") |> range(start: " + start + "  , stop: " + finish + ") |> filter(fn: (r) => r[\"_measurement\"] == \"EcowattDataHeure\")  |> filter(fn: (r) => r[\"_field\"] == \"value_heure'\")";
        List<FluxTable> tables = influxDBClient.getQueryApi().query(query, org);

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                System.out.println(record.getValues().get("_time").toString() + " " + record.getValues().get("_value").toString());
            }
        }

    }
}
