package sample.Model;


public class FingerPrintSensor {
    Arduino arduino;
    String port;
    int baud_rate;

    public FingerPrintSensor(String port, int baud_rate) {
        this.port = port;
        this.baud_rate = baud_rate;
    }

    public void create(){
        arduino = new Arduino(port, baud_rate);
    }
    public boolean connect(){
        return arduino.openConnection();
    }
    public void write(String input){
        arduino.serialWrite(input);
    }
    public String read(){
        String readed = arduino.serialRead();
        while((readed.equals("")) || (readed.equals("\n"))) {
            readed = arduino.serialRead();
        }
        return readed;
    }
    public int search(){
        System.out.println(read());
        write("2");
        System.out.println(read());
        return Integer.parseInt(read());
    }
    public boolean delete(int id){
        System.out.println(read());
        write("3");
        System.out.println(read());
        write(id + "");
        return read().contains("Deleted");
    }
    public boolean enroll(int id){
        System.out.println(read());
        write("1");
        System.out.println(read());
        write(id + "");
        String string = "";
        while (!(string += read()).contains("Stored"));
        return string.contains("Imagetaken" +
                "ImageconvertedRemovefinger" +
                "Again" +
                "Imageconverted" +
                "MatchedStored!");
    }
    public void disconnect(){
        arduino.closeConnection();
    }

}
