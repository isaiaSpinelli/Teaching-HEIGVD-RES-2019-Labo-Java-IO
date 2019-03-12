package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private int numberOfLine = 1;
  private boolean charBackSlashR = false;

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    write(str.toCharArray(),off,len);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for(int i=off; i < (len + off); ++i){
      write(cbuf[i]);
    }
  }

  @Override
  public void write(int c) throws IOException {

    if(numberOfLine == 1){
      writeNumberOfLine();
      super.write(c);
    }
    else {
      if (c == '\n'){
        super.write(c);
        writeNumberOfLine();
      }
      else{
          if (charBackSlashR){
              writeNumberOfLine();
          }
          super.write(c);
      }
    }

    if(c == '\r'){
      charBackSlashR = true;
    }
    else{
      charBackSlashR = false;
    }


  }

    // Permet d'écrire le numéro de la ligne
    private void writeNumberOfLine() throws IOException {
        String num = String.valueOf(numberOfLine++);
        super.write(num,0,num.length());
        super.write('\t');
    }

}
