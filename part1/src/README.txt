
Η υλοποιήση της εργασίας έγινε στο idle Intellij. 
Μπορούμε να το τρέξουμε είτε από εκεί είτε από το τερματικό (cmd), κατα προτιμήση από Intellij.
!Πριν τρέξουμε το πρόγραμμα θα πρέπει να ελέγξουμε οτι τα αρχεία publishers.txt, publishers_info.txt είναι άδεια!
Για το τερματικό θα πρέπει να ανοίξουμε 6 διαφορετικά cmd, βάζοντας το σωστό Path για τα τρια θα κάνουμε javac Broker.java και για τα άλλα 3 javac UserNode.java
Συγκεκριμένα τα βήματα που πρέπει να ακολουθήσουμε είναι:
javac Broker.java
java Broker 127.0.0.1 4321

javac Broker.java
java Broker 127.0.0.3 7588

javac Broker.java
java Broker 127.0.0.2 6547

javac UserNode.java
java UserNode 127.0.0.7 300

javac UserNode.java
java UserNode 127.0.0.7 400

javac UserNode.java
java UserNode 127.0.0.7 500

Στο intellij για ευκολία και για λίγοτερο χρόνο απλα αντικαθιστούσαμε χειροκίνητα τα ip, ports εκει που είναι τα args[0],args[1]

Για τον χρήστη με το port 300 αντιστοιχούν τα αρχεία:
babyyoda.mp4
monkey.mp4
pic.png
plane.png
skin.png

Για τον χρήστη με το port 400 αντιστοιχούν τα αρχεία:
cocktails.png
rainbow.png
snow.mp4
tiktok.mp4
wine.mp4

Για τον χρήστη με το port 500 αντιστοιχούν τα αρχεία:
cat.mp4
kitties.mp4
meme.mp4
pokemon.png
sky.png

