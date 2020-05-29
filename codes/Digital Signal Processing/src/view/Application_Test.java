/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import acquisition.Wave_Audio_Signal;
import java.awt.List;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 *
 * @author hiago
 */
public class Application_Test {
        
    public static Wave_Audio_Signal[] fill_and_calculate_emotions_lists(String emotion){
        File[] returned_list;
        String code= "";
        if(emotion.equalsIgnoreCase("anger")){
            code= "W";
        }else if(emotion.equalsIgnoreCase("boredom")){
            code= "L";
        }else if(emotion.equalsIgnoreCase("disgust")){
            code= "E";
        }else if(emotion.equalsIgnoreCase("fear")){
            code= "A";
        }else if(emotion.equalsIgnoreCase("happiness")){
            code= "F";
        }else if(emotion.equalsIgnoreCase("neutrality")){
            code= "N";
        }else if (emotion.equalsIgnoreCase("sadness")){
            code= "T";
        }
        
        returned_list= apply_filter_list(code);
        Wave_Audio_Signal[] obj= new Wave_Audio_Signal[returned_list.length];
        for(int i=0; i<returned_list.length; i++){ 
            obj[i]= new Wave_Audio_Signal();
            obj[i].wave_file_reader(returned_list[i].toString(), obj[i]);
        }
        return obj;
    }
    
    public static File[] apply_filter_list(String code){
        File f= new File("D:\\Hiago\\Acadêmico\\PÓS GRADUAÇÃO\\Disciplinas\\Estudo - PDS\\Pesquisa - A partir de 09.08.2019 (De Fato)\\Bancos de Dados\\EMO-DB\\wav\\Full DB");
        File[] emotion_list= f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.contains(code)){
                    return true;
                }else{
                    return false;
                }
            }
        });
        return emotion_list;
    }
    
    public static void main(String[] args) {
        Wave_Audio_Signal[] anger_waves;
        anger_waves= fill_and_calculate_emotions_lists("anger");
        System.out.println("tamanho da lista: "+anger_waves.length);
        
        //it is working in the right way - do for all emotions and after this i can create the feature matrix based on energy


//        for(int j=0; j<anger_waves[0].getSignal_lenght(); j++){
//            System.out.println("S["+j+"] = "+anger_waves[0].getSignal()[j]);
//        }
    }
    
    
}
