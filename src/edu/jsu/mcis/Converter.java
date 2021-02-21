package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE

            JSONArray c_header = new JSONArray();
            
            String column_header[] = iterator.next();
            int c = 0;
            while(c < column_header.length)
            {
                c_header.add(column_header[c]);
                c++;
            }
            
            String[] ind_line;
            
            JSONArray r_header = new JSONArray();
            
            JSONArray data = new JSONArray();
            
            JSONObject students = new JSONObject();

            
            while (iterator.hasNext()){
                int i = 1;
                ind_line = iterator.next();
                r_header.add(ind_line[0]);
                JSONArray ldata = new JSONArray();
                while(i < ind_line.length)
                {                    
                    ldata.add(Integer.parseInt(ind_line[i]));
                    i++;
                }
                data.add(ldata);
            }

           
            students.put("colHeaders", c_header);
            students.put("rowHeaders", r_header);
            students.put("data", data);
            

            results = JSONValue.toJSONString(students);

        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // INSERT YOUR CODE HERE
            
           JSONParser parser = new JSONParser();
           JSONObject jsonobject = (JSONObject)parser.parse(jsonString);
           
           JSONArray listh = new JSONArray();       //for entire string
           //col.add(jsonobject.get("colHeaders"));
           
           String col = jsonobject.get("colHeaders").toString();
           String nobr = col.substring(1, (col.length() - 1));
           //String column[] = nobr.split(",");
           listh.add(nobr);
           //writer.append(nobr);
           
           
           String[] keys = nobr.split(",");
           
           int d = 0;
           
           while(d < keys.length)
           {
               if(keys[d].contains("\""))
               {
                   int start = keys[d].indexOf("\"");
                   keys[d] = keys[d].substring((start + 1));
                   
                   if(keys[d].contains("\""))
                   {
                       int last = keys[d].indexOf("\"");
                       keys[d] = keys[d].substring(0, (last));
                   }
               }
               //writer.append(keys[d]);
               d++;
           }
           //writer.append("hhhh");
           
           //results = writer.toString();
           csvWriter.writeNext(keys);

           //System.out.println(writer);
                   
           String rowh = jsonobject.get("rowHeaders").toString();
           String rowbr = rowh.substring(1, (rowh.length() - 1));
           String[] rowhead = rowbr.split(",");
           
           int e = 0;
           
           while(e < rowhead.length)
           {
               if(rowhead[e].contains("\""))
               {
                   int beg = rowhead[e].indexOf("\"");
                   rowhead[e] = rowhead[e].substring((beg + 1));
                   
                   if(rowhead[e].contains("\""))
                   {
                       int end = rowhead[e].indexOf("\"");
                       rowhead[e] = rowhead[e].substring(0, end);
                   }
               }
               e++;
           }

           String dataj = jsonobject.get("data").toString();
           String datarr[] = dataj.split(",");
           int y = 0;
           while(y < datarr.length)
           {
                
                if(datarr[y].contains("["))
                {
                    int remove = datarr[y].indexOf("[");
                    datarr[y] = datarr[y].substring((remove + 1));
                    if(datarr[y].contains("["))
                    {
                        remove = datarr[y].indexOf("[");
                        datarr[y] = datarr[y].substring((remove + 1));
                    }
                }
                else if(datarr[y].contains("]"))
                {

                    int r = datarr[y].indexOf("]");
                    datarr[y] = datarr[y].substring(0, r);
                }
                y++;
           }           
           
           int h = 0;
           int j = 0;
           String line[] = new String[5];
           while(h < datarr.length)
           {

               line[0] = rowhead[j];
               
               line[1] = datarr[h];
               h++;
               line[2] = datarr[h];
               h++;
               line[3] = datarr[h];
               h++;
               line[4] = datarr[h];
               h++;
               if(j<7)
               {
                    j++;
               }
               csvWriter.writeNext(line);
           }
           
           String csvString = writer.toString();
           results = csvString;
           //String csvString = writer.toString();
           //System.out.println(csvWriter.toString());
           
           /*JSONObject obb = new JSONObject();
           
           
           String col = jsonobject.get("colHeaders").toString();
           String nobr = col.substring(1, (col.length() - 1));
           String column[] = nobr.split(",");
           
           String row = jsonobject.get("rowHeaders").toString();
           String rowbr = row.substring(1, (row.length() - 1));
           String rowarr[] = rowbr.split(",");
           
           String dataj = jsonobject.get("data").toString();
           String datarr[] = dataj.split(",");
           int y = 0;
           while(y < datarr.length)
           {
                
                if(datarr[y].contains("["))
                {
                    int remove = datarr[y].indexOf("[");
                    datarr[y] = datarr[y].substring((remove + 1));
                    if(datarr[y].contains("["))
                    {
                        remove = datarr[y].indexOf("[");
                        datarr[y] = datarr[y].substring((remove + 1));
                    }
                }
                else if(datarr[y].contains("]"))
                {

                    int r = datarr[y].indexOf("]");
                    datarr[y] = datarr[y].substring(0, r);
                }
                y++;
           }
           for(int z = 0; z < datarr.length; z++)
           {
             System.out.println(datarr[z]);
           }
           
           int lnum = 0;
           String line = rowarr[0];*/
           //line[lnum]= rowarr[lnum];           
           

           
           
           
        }                        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}