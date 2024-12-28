package software.ulpgc.kata4.control;

import software.ulpgc.kata4.io.*;
import software.ulpgc.kata4.model.Movie;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ImportCommand implements Command {
    public ImportCommand(){
    }

    @Override
    public void execute() throws Exception {
        File inputFile = getInputFile();
        File outputFile = getOutputFile();
        doExecute(inputFile, outputFile);
    }

    private void doExecute(File inputFile, File outputFile) throws Exception {
        try(MovieWriter movieWriter = createMovieWriter(outputFile);
            MovieReader movieReader = createMovieReader(inputFile)){
            while(true) {
                Movie movie = movieReader.read();
                if (movie == null) break;
                movieWriter.write(movie);
            }
        }
    }

    private static MovieReader createMovieReader(File inputFile) throws IOException {
        return new FileMovieReader(new TsvMovieDeserializer(), inputFile);
    }

    private static MovieWriter createMovieWriter(File outputFile) throws SQLException {
        return new DatabaseMovieWriter(deleteIfExists((outputFile)));
    }

    private static File deleteIfExists(File outputFile) {
        if(outputFile.exists()) outputFile.delete();
        return outputFile;
    }

    private static File getOutputFile() {
        return new File("movies.db");
    }

    private static File getInputFile() {
        return new File("C:\\Users\\User\\Desktop\\katais2\\recursos\\title.basics.tsv");
    }
}
