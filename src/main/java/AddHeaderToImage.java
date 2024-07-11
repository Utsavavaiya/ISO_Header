import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.io.IOException;

public class AddHeaderToImage {
    public static void main(String[] args) {
        // Replace with the paths to your input and output directories
        Path inputDir = Paths.get("C:\\Research\\IIT B\\Fingerprint\\Code\\Priyanshu_data\\CB\\jp2");
        Path outputDir = Paths.get("C:\\Research\\IIT B\\Fingerprint\\Code\\Priyanshu_data\\CB\\iso");

        try {
            // Create output directory if it does not exist
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            // Step 1: Instantiate the class
            FingerImageToIso fingerImageToIso = new FingerImageToIso();

            // Step 2: Iterate over the files in the input directory
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, "*.jp2")) {
                for (Path inputPath : stream) {
                    // Define the output path for the converted image
                    Path outputPath = outputDir.resolve(inputPath.getFileName().toString().replace(".jp2", ".iso"));

                    // Step 3: Read the image file into a byte array
                    byte[] imageBytes = Files.readAllBytes(inputPath);

                    // Step 4: Add the header to the image
                    byte[] imageWithHeader = fingerImageToIso.getFingerHeaderWithImage(imageBytes);

                    // Step 5: Save the new byte array to a file
                    Files.write(outputPath, imageWithHeader);

                    System.out.println("Image with header saved successfully to " + outputPath.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
