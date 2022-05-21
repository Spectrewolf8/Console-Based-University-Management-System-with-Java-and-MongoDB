import java.util.Scanner;
import java.util.HashMap;


public class MainTestPortalProject {

    static Scanner scanner = new Scanner(System.in);//Global Scanner For Inputs Anywhere inside program

//Salman's Workspace
    public static void Quizz(){
        System.out.print("Choose any option : \ta : Students Section\tb : Teachers Section\tc : Administrative section");
        String choice = scanner.next();
        if("Aa".contains(choice)){
            StudentQuizSection();
        }else if("Bb".contains(choice)){
            TeacherQuizSection();
        }else if("Cc".contains(choice)){
            AdminQuizSection();
        }
    }

    private static void AdminQuizSection() {
    }

    private static void TeacherQuizSection() {
    }

    public static void StudentQuizSection(){

    }
//Salman's Workspace Ending

//Oumar's Workspace Starting
//Oumar's Workspace Ending

//Humayun's Workspace Starting
//Humaayun's Workspace Ending

//Main Workspace(Collective)
    public static void main(String[] args) {


    }
}
