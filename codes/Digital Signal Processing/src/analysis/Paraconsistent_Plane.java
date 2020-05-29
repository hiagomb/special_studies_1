/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis;

/**
 *
 * @author hiago
 */
public class Paraconsistent_Plane {
    
    private double g1;
    private double g2;
    private double distance_from_right;
    private double distance_from_left;
    private double distance_from_up;
    private double distance_from_down;

    public Paraconsistent_Plane() {
    }

    public Paraconsistent_Plane(double g1, double g2, double distance_from_right, double distance_from_left, double distance_from_up, double distance_from_down) {
        this.g1 = g1;
        this.g2 = g2;
        this.distance_from_right = distance_from_right;
        this.distance_from_left = distance_from_left;
        this.distance_from_up = distance_from_up;
        this.distance_from_down = distance_from_down;
    }

    public double getG1() {
        return g1;
    }

    public void setG1(double g1) {
        this.g1 = g1;
    }

    public double getG2() {
        return g2;
    }

    public void setG2(double g2) {
        this.g2 = g2;
    }

    public double getDistance_from_right() {
        return distance_from_right;
    }

    public void setDistance_from_right(double distance_from_right) {
        this.distance_from_right = distance_from_right;
    }

    public double getDistance_from_left() {
        return distance_from_left;
    }

    public void setDistance_from_left(double distance_from_left) {
        this.distance_from_left = distance_from_left;
    }

    public double getDistance_from_up() {
        return distance_from_up;
    }

    public void setDistance_from_up(double distance_from_up) {
        this.distance_from_up = distance_from_up;
    }

    public double getDistance_from_down() {
        return distance_from_down;
    }

    public void setDistance_from_down(double distance_from_down) {
        this.distance_from_down = distance_from_down;
    }
    
    
    public void paraconsistent_analyser(double[][] feature_matrix, int number_of_classes, Paraconsistent_Plane obj){
        
        int rows= feature_matrix.length;
        int columns= feature_matrix[0].length;
        double alfa=0, beta=0;
        double major=0, minor=0;
        double[][] similarity_matrix= new double[number_of_classes][columns-1];
        double[][] intervals_matrix= new double[number_of_classes*2][columns-1];  
        
        //i am supposing that the last column corresponds to the label of the sample (1S)
        //i am also supposing that samples are ordered according to their class (2S)
        
        //calculating the value of alfa firstly = intra class similarity
        int aux_class=0, aux_intervals_rows=0, aux_intervals_columns=0;
        for(int c=0; c<number_of_classes; c++){  
            for(int j=0; j<columns-1; j++){ //(2S)
                major= Double.MIN_VALUE; minor= Double.MAX_VALUE;
                for(int i=0; i<rows; i++){
                    if(feature_matrix[i][columns-1]== aux_class){ //i am verifying the label of the class (1S)
                        if(feature_matrix[i][j]> major){
                            major= feature_matrix[i][j];
                        }
                        if(feature_matrix[i][j]<minor){
                            minor= feature_matrix[i][j];
                        }
                    }
                }
                double Y= 1 - (major-minor); similarity_matrix[c][j]= Y;
                //I also use min and max to calculate beta with intervals
                intervals_matrix[aux_intervals_rows][aux_intervals_columns]= major; 
                intervals_matrix[aux_intervals_rows][aux_intervals_columns+1]= minor;
                aux_intervals_rows+=1;
            }
            aux_class+=1; 
        }
        
        //here i have the similarity vectors, now I need to calculate an avg of each line and after get the min value
        double[] avg_sim= new double[number_of_classes]; double min_avg= Double.MAX_VALUE;
        for(int i=0; i<number_of_classes; i++){
            double sum=0;
            for(int j=0; j<columns-1; j++){
                avg_sim[i]+=similarity_matrix[i][j]/(columns-1); //calculating the avg
            }
            if(avg_sim[i]< min_avg){
                min_avg= avg_sim[i];
            }      
        }
        alfa= min_avg; //ALFA IS CALCULATED
        //-----------------now I have to calculate the beta value with intervals_matrix----------------------------------
        
        int overlaps= 0; //corresponds to R value in the paper 
        int F= number_of_classes* (number_of_classes-1)*(rows/number_of_classes)*(columns-1);
        for(int i=0; i<number_of_classes*2; i++){//verifyng if there are overlaps among max and min of the same characteristic
            for(int k=i; k<number_of_classes*2; k++){ 
                if(k!=i){
                    if((i%2==0 && k%2==0) || (i%2!=0 && k%2!=0)){ // I cannot compare different characteristics
                        if((intervals_matrix[i][0]< intervals_matrix[k][0]) && (intervals_matrix[i][0]> intervals_matrix[k][1])){
                            overlaps+=1;
                        }
                        if((intervals_matrix[i][1]< intervals_matrix[k][0]) && (intervals_matrix[i][1]> intervals_matrix[k][1])){
                            overlaps+=1;
                        }
                    }
                }
            }
        }
        beta= overlaps/F; //BETA IS CALCULATED
        
        obj.setG1(alfa-beta);
        obj.setG2(alfa+beta-1);
        obj.setDistance_from_right(Math.sqrt((Math.pow(obj.getG1()-1, 2))+ (Math.pow(obj.getG2(), 2))));
        obj.setDistance_from_up(Math.sqrt((Math.pow(obj.getG2()-1, 2))+ (Math.pow(obj.getG1(), 2))));
        obj.setDistance_from_left(Math.sqrt((Math.pow(obj.getG1()+1, 2))+ (Math.pow(obj.getG2(), 2))));
        obj.setDistance_from_down(Math.sqrt((Math.pow(obj.getG2()+1, 2))+ (Math.pow(obj.getG1(), 2))));        
    }
    
    
//    public static void main(String[] args) {
//        
//        double[][] matrix= new double[12][3];
//        
//        matrix[0][0]= 0.90; matrix[0][1]= 0.12; matrix[0][2]= 0;
//        matrix[1][0]= 0.88; matrix[1][1]= 0.14; matrix[1][2]= 0;
//        matrix[2][0]= 0.88; matrix[2][1]= 0.13; matrix[2][2]= 0;
//        matrix[3][0]= 0.89; matrix[3][1]= 0.11; matrix[3][2]= 0;
//        
//        matrix[4][0]= 0.55; matrix[4][1]= 0.53; matrix[4][2]= 1;
//        matrix[5][0]= 0.53; matrix[5][1]= 0.55; matrix[5][2]= 1;
//        matrix[6][0]= 0.53; matrix[6][1]= 0.54; matrix[6][2]= 1;
//        matrix[7][0]= 0.56; matrix[7][1]= 0.54; matrix[7][2]= 1;
//        
//        matrix[8][0]= 0.10; matrix[8][1]= 0.88; matrix[8][2]= 2;
//        matrix[9][0]= 0.11; matrix[9][1]= 0.86; matrix[9][2]= 2;
//        matrix[10][0]= 0.12; matrix[10][1]= 0.87; matrix[10][2]= 2;
//        matrix[11][0]= 0.11; matrix[11][1]= 0.88; matrix[11][2]= 2;
//        
//        Paraconsistent_Plane obj= new Paraconsistent_Plane();
//        obj.paraconsistent_analyser(matrix, 3, obj);
//        
//        System.out.println("valor de G1: "+obj.getG1());
//        System.out.println("valor de G2: "+obj.getG2());
//        
//        System.out.println("distancia para a direita: "+obj.getDistance_from_right());
//        System.out.println("distancia para cima: "+obj.getDistance_from_up());
//        System.out.println("distancia para a esquerda: "+obj.getDistance_from_left());
//        System.out.println("distancia para baixo: "+obj.getDistance_from_down());
//        
//    }
    
}
