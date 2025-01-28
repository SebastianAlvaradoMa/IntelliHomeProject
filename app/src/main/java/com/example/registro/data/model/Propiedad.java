package com.example.registro.data.model;
import org.json.JSONException;
import org.json.JSONObject;
public class Propiedad {
    //Datos registro propiedad
    private String nombrePropiedad;
    private Integer precio;
    private String no;
    private String si;
    private String contacto2;
    private String personasmax2;
    private String amenidades1;
    private String amenidades2;
    private String amenidades3;
    private String amenidades4;
    private String txtlatitud;
    private String txtlongitud;
    private String fotoPropiedad;

    public Propiedad(String nombrePropiedad, Integer precio, String no, String si, String contacto2, String personasmax2, String amenidades1, String amenidades2, String amenidades3, String amenidades4, String txtlatitud, String txtlongitud, String camara) {
        this.nombrePropiedad = nombrePropiedad;
        this.precio = precio;
        this.no = no;
        this.si = si;
        this.contacto2 = contacto2;
        this.personasmax2 = personasmax2;
        this.amenidades1 = amenidades1;
        this.amenidades2 = amenidades2;
        this.amenidades3 = amenidades3;
        this.amenidades4 = amenidades4;
        this.txtlatitud = txtlatitud;
        this.txtlongitud = txtlongitud;
        this.fotoPropiedad = fotoPropiedad;
    }

    //Creacion del JSON
    public String toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject payloadObject = new JSONObject();

            // Add fields to the payload
            payloadObject.put("nombrePropiedad", nombrePropiedad);
            payloadObject.put("precio", precio);
            payloadObject.put("no", no);
            payloadObject.put("si", si);
            payloadObject.put("contacto2", contacto2);
            payloadObject.put("personasmax2", personasmax2);
            payloadObject.put("amenidades1", amenidades1);
            payloadObject.put("amenidades2", amenidades2);
            payloadObject.put("amenidades3", amenidades3);
            payloadObject.put("amenidades4", amenidades4);
            payloadObject.put("txtlatitud", txtlatitud);
            payloadObject.put("txtlongitud", txtlongitud);
            payloadObject.put("fotoPropiedad", fotoPropiedad);

            // Wrap in the client request format
            jsonObject.put("action", "REGISTERPROPIEDAD");
            jsonObject.put("payload", payloadObject);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}"; // Return an empty JSON object in case of an error
        }
    }

    //Getters
    public String getNombrePropiedad() { return nombrePropiedad; }
    public Integer getPrecio() { return precio; }
    public String getNo() { return no; }
    public String getSi() { return si; }
    public String getContacto2() { return contacto2; }
    public String getPersonasmax2() { return personasmax2; }
    public String getAmenidades1() { return amenidades1; }
    public String getAmenidades2() { return amenidades2; }
    public String getAmenidades3() { return amenidades3; }
    public String getAmenidades4() { return amenidades4; }
    public String getTxtlatitud() { return txtlatitud; }
    public String getTxtlongitud() { return txtlongitud; }
    public String getFotoPropiedad() { return  fotoPropiedad; }
}
