package ch.heigvd.res.labio.impl.transformers;

import ch.heigvd.res.labio.interfaces.IFileVisitor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This abstract class implements the IFileVisitor interface and has the responsibility
 * to open an input text file, to read its content, to apply a number of transformations
 * (via filters) and to write the result in an output text file.
 * 
 * The subclasses have to implement the decorateWithFilters method, which instantiates
 * a list of filters and decorates the output writer with them.
 * 
 * @author Olivier Liechti
 */
public abstract class FileTransformer implements IFileVisitor {

  private static final Logger LOG = Logger.getLogger(FileTransformer.class.getName());
  private final List<FilterWriter> filters = new ArrayList<>();
  
  /**
   * The subclasses implement this method to define what transformation(s) are
   * applied when writing characters to the output writer. The visit(File file)
   * method creates an output file and creates a corresponding writer. It then
   * calls decorateWithFilters and passes the writer as argument. The method
   * wraps 0, 1 or more filter writers around the original writer and returns 
   * the result.
   * 
   * @param writer the writer connected to the output file
   * @return the writer decorated by 0, 1 or more filter writers
   */
  public abstract Writer decorateWithFilters(Writer writer);

  @Override
  public void visit(File file) {
    if (!file.isFile()) {
      return;
    }
    try {
      Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
      Writer writer = new OutputStreamWriter(new FileOutputStream(file.getPath()+ ".out"), StandardCharsets.UTF_8); // the bug fix by teacher
      writer = decorateWithFilters(writer);

      /*
       * There is a missing piece here: you have an input reader and an ouput writer (notice how the 
       * writer has been decorated by the concrete subclass!). You need to write a loop to read the
       * characters and write them to the writer.
       */
      
      int b;
      while((b = reader.read()) != -1){
         writer.write(b);
      }
      
      reader.close();
      writer.flush();
      writer.close();
    } catch (IOException ex) {
      LOG.log(Level.SEVERE, null, ex);
    }
  }

}
