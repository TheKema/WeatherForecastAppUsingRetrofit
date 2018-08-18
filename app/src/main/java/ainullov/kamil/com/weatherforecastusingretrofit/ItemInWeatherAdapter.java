package ainullov.kamil.com.weatherforecastusingretrofit;

public class ItemInWeatherAdapter {
    private String date;
    private String desc;
    private String icon;
    private float temp;

    public ItemInWeatherAdapter(String date, String desc, String icon, float temp) {
        this.date = date;
        this.desc = desc;
        this.icon = icon;
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }
}
