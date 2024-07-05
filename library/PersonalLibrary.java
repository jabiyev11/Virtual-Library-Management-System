package library;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class PersonalLibrary {

    private static final String PERSONAL_LIBRARY_CSV_FILE_PATH = "data/PersonalLibraryData.csv";
    public static void updatePersonalLibraries(Book originalBook, Book updatedBook){

        try {
            Path filePath = Paths.get(PERSONAL_LIBRARY_CSV_FILE_PATH);
            List<String> lines = Files.readAllLines(filePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PERSONAL_LIBRARY_CSV_FILE_PATH))) {

                for(String line : lines){
                    String[] parts = line.split(",");

                    System.out.println("Checking line: " + line);
                    System.out.println("Parts: " + Arrays.toString(parts));
                    System.out.println("Original Book: " + originalBook);


                    if(parts.length > 4 &&
                    parts[1].equals(originalBook.getTitle()) &&
                    parts[2].equals(originalBook.getAuthor()) &&
                    parts[3].equals(originalBook.getBookGenre().toString()) &&
                    parts[4].equals(originalBook.getPublicationDate().toString())){

                        writer.write(parts[0] + "," +
                                updatedBook.getTitle() + "," +
                                updatedBook.getAuthor() + "," +
                                updatedBook.getBookGenre() + "," +
                                updatedBook.getPublicationDate() + ",");

                        System.out.println("Updating book: " + originalBook + " to " + updatedBook);
                    }else{

                        writer.write(line);
                    }
                    writer.newLine();
                }

            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error in updating book info in personal library");
        }
    }


    public static void removeBookFromPersonalLibraries(Book bookToRemove){

        try {
            Path filePath = Paths.get(PERSONAL_LIBRARY_CSV_FILE_PATH);
            List<String> lines = Files.readAllLines(filePath);

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(PERSONAL_LIBRARY_CSV_FILE_PATH))){

                for(String line : lines){
                    String[] parts = line.split(",");

                    if(!(parts.length > 4 &&
                            parts[1].equals(bookToRemove.getTitle()) &&
                            parts[2].equals(bookToRemove.getAuthor()) &&
                            parts[3].equals(bookToRemove.getBookGenre().toString()) &&
                            parts[4].equals(bookToRemove.getPublicationDate().toString()))){

                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error during removing book info from personal library");
        }

    }
}
