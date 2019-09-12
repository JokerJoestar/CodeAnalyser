# CodeAnalyser
Java Code Analyser

Οδηγίες:
* Ο φάκελος eclipse-workspace περιέχει δύο φακέλους με τα στάδια εκτέλεσης του κώδικα.
* Ο φάκελος runtime-EclipseApplication περιέχει απλά ένα dummy project για την εμφάνιση των στοιχείων.
* Για την εκτέλεση του project gr.teilar.codeanalyser.hcdiagram, θα χρειαστεί η εισαγωγή της βιβλιοθήκης RCaller
  στο buildpath του project. Το .jar αρχείο του RCaller βρίσκεται στον φάκελο gr.teilar.codeanalyser.hcdiagram.
* Η εκτέλεση του project gr.teilar.codeanalyser παράγει ένα αρχείο output.txt στον home φάκελο του χρήστη 
  με τον πίνακα ομοιότητας των πακέτων που διάβασε από το project στο οποίο έτρεξε.
* Η εκτέλεση του project gr.teilar.codeanalyser.hcdiagram διαβάζει το αρχείο output.txt από τον home φάκελο του 
  χρήστη και εμφανίζει σε ένα νέο παράθυρο το διάγραμμα με την ιεραρχική συσταδοποίηση.
