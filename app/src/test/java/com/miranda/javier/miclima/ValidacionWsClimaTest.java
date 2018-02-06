package com.miranda.javier.miclima;

import com.miranda.javier.miclima.ws.ClimaWS;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ValidacionWsClimaTest {
    @Test
    public void longitudYlatitudGuayaquil() throws Exception {
        String data = new ClimaWS().getClima(-2.2453914,-79.8894987);
        JSONObject jsonObject = new JSONObject(data);
        String ciudad=jsonObject.getString("timezone");
        String expected = "America/Guayaquil";
        assertEquals("La localizacion no coincide con la ciudad", expected, ciudad);
    }


    @Test
    public void direccionInvalida() throws Exception {
        String data = new ClimaWS().getClima(123.00,12.22);
        String expected = "Respuesta del servidor con codigo: 400";
        assertEquals("Localizacion Valida", expected, data);
    }
}