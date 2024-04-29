/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Experiment;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.Arrays;

/**
 *
 * @author jayia
 */
public class Experiment {
    
    final static int sampleTotal = 569;  //total number of samples

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//declaring file object that will be read into array
        String path ="C:\\Users\\jayia\\OneDrive\\Documents\\Programming\\Java\\Experiment\\Experiment.csv";
        String line="";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            
            double[][] dataArray = new double[sampleTotal][32];

            //Load Double Array with neccessary data
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                for (int j = 2; j < values.length; j++) {
                    dataArray[row][j] = Double.parseDouble(values[j]);
//                     System.out.print(dataArray[row][i] + " ");
//                    System.out.println("col: "+ i);
                }
                row++;
//                System.out.println();
            }
             reader.close();
             
            SampleDistance test = new SampleDistance();
            
            test.calculateSampleDistance(dataArray, path);
           
           
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }
    
    
}

class Distance{
    final static int sampleTotal = 569;
    
    double returnDistance(double[] arr1, double[] arr2){
        
        double sum=0;
        
        for (int i=0; i<arr1.length; i++){
            
            sum= sum + Math.pow(arr1[i]-arr2[i], 2);
        }
        
        return Math.sqrt(sum);
    }
}

class SampleDistance extends Distance{
    
    private double[][] distanceArr;
    
    //calculating distance between each sample
    
    void calculateSampleDistance(double[][] array, String path) throws FileNotFoundException, IOException{
       
        distanceArr = new double[array.length][array.length];
        
        for (int i=0; i<array.length; i++){
            for (int j=0; j<array.length; j++){
                 distanceArr[i][j] = returnDistance(array[i], array[j]);
//                System.out.print(distanceArr[i][j]+ ",");
               
            }
//            System.out.println("");
        }
        calculateTopN(distanceArr, path);
       
    }
    
    //calculating top n
    void calculateTopN(double[][] distanceArr, String path) throws FileNotFoundException, IOException{
        
        String[] diagnoses = new String[sampleTotal];
        BufferedReader diagnosisReader = new BufferedReader(new FileReader(path));
        String line = "";

        // Reading file for diagnosis of each sample into 1d array
        int col = 1; // this is the column for the diagnoses
        int row = 0;
        while ((line = diagnosisReader.readLine()) != null && row < sampleTotal) {
            String[] values = line.split(",");
            diagnoses[row] = values[col];
            row++;
        }
        diagnosisReader.close();
        
        
        for (int N=3; N<14; N=N+2){ //N being number of closest samples
            //important variables
            System.out.println("---------------------");
            double accuracy=0; //accuracy of predictions per N
            int correctTotal=0; //number of correct predictions per N
        
        for (int i = 0; i < distanceArr.length; i++){
                //sorting each row by distance
                double[] distanceArr1 = distanceArr[i].clone(); //duplicating distance array for sorting
                Arrays.sort(distanceArr1);
               
                //finding N closest samples and diagnoses
//                System.out.println(N + " closest samples to sample " + (i+1) + ":");
                
              int Mcount=0; //keep count of diagnosis M for each closest sample
              int Bcount=0; //keep count of diagnosis B for each closest sample
                
                for (int n = 1; n <= N; n++){
                    for (int j = 0; j < distanceArr[i].length; j++){

                        if (distanceArr[i][j] == distanceArr1[n]){
                            
//                            System.out.println((j+1) + ": " + diagnoses[j]);
                            
                            //setting up to make prediction
                            if(diagnoses[j].equals("B")){
                                Bcount++;
                            }
                            else if(diagnoses[j].equals("M")){
                                Mcount++;
                            }
                            
                        }
                    }  
                }
                
//                System.out.println(diagnoses[i] + " M: "+ Mcount + " B: " + Bcount);
                //predictions
                if(Bcount>Mcount && diagnoses[i].equals("B")){
                    correctTotal++;
//                    System.out.println(correctTotal);
                }else if(Bcount<Mcount && diagnoses[i].equals("M") ){
                    correctTotal++;
//                    System.out.println(correctTotal);
                }
//                else{
//                    System.out.println("incorrect");
//                }
                
                //System.out.println(); // empty line after each sample's closest samples
            }
            accuracy = ((double) correctTotal / distanceArr.length) * 100;
            System.out.println("Correct Predictions: " + correctTotal);
            System.out.print(String.format("N: %d Accuracy: %.1f", N, accuracy));
            System.out.println("%");
        }
        
    }
        
        
        
        
    }
    
    

