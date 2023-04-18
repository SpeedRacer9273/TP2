package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import server.Course;
import server.RegistrationForm;
import server.Server;

class client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", Server.PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            while (running) {
                System.out.println("Que voulez-vous faire? (charger/inscrire/quitter)");
                String command = scanner.nextLine();
                if (command.equals(Server.LOAD_COMMAND)) {
                    System.out.println("Pour quelle session voulez-vous charger la liste des cours?");
                    String session = scanner.nextLine();
                    objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + session);
                    objectOutputStream.flush();
                    ArrayList<Course> courses = (ArrayList<Course>) objectInputStream.readObject();
                    System.out.println("Liste des cours pour la session " + session + ":");
                    for (Course course : courses) {
                        System.out.println(course.getCode() + " - " + course.getName());
                    }
                } else if (command.equals(Server.REGISTER_COMMAND)) {
                    System.out.println("Veuillez entrer votre nom:");
                    String name = scanner.nextLine();
                    System.out.println("Veuillez entrer votre adresse courriel:");
                    String email = scanner.nextLine();
                    System.out.println("Pour quelle session voulez-vous vous inscrire?");
                    String session = scanner.nextLine();
                    System.out.println("Quel cours voulez-vous suivre?");
                    String course = scanner.nextLine();
                    RegistrationForm form = new RegistrationForm(name, email, session, course);
                    objectOutputStream.writeObject(Server.REGISTER_COMMAND);
                    objectOutputStream.flush();
                    objectOutputStream.writeObject(form);
                    objectOutputStream.flush();
                    String message = (String) objectInputStream.readObject();
                    System.out.println(message);
                } else if (command.equals("quitter")) {
                    running = false;
                } else {
                    System.out.println("Commande invalide.");
                }
            }
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
