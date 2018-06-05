package sample.Model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Entity
@Table(name = "candidates")
public class Candidate extends Human {

    @Column(name = "description")
    private String description;

    @Column(name = "imagePath")
    private String imagePath;

    @Column(name = "electionid")
    private int electionid;

    @Column(name = "votecounter")
    private int votecounter;

    @Transient
    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImage(){
        try {
            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(new File(imagePath));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView = new ImageView(image);
            double height = image.getHeight() * 0.7;
            while (height > 100){
                height *= 0.9;
            }
            double width = image.getWidth() * 0.7;
            while (width > 100){
                width *= 0.9;
            }
            imageView.setFitHeight(height);
            imageView.setFitWidth(width);
//            imageView.setImage(image);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Candidate() {
        //setImage();
    }

    public Candidate(String name, String surname, long identityNumber, String password, String street,
                     String number, String town, String city, String description, String imagePath,
                     int electionid, int votecounter) {
        super(name, surname, identityNumber, password, street, number, town, city);
        this.description = description;
        this.imagePath = imagePath;
        this.electionid = electionid;
        this.votecounter = votecounter;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getElectionid() {
        return electionid;
    }

    public void setElectionid(int electionid) {
        this.electionid = electionid;
    }

    public int getVotecounter() {
        return votecounter;
    }

    public void setVotecounter(int votecounter) {
        this.votecounter = votecounter;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
