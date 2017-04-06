package smarthousesimulator;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/*
Realtime simulacija pametne kuce, preko FIREBASE realtime baze podataka
Radi u paru sa android aplikacijom koja iscitava simulirane vrijednosti
simuliramo izmjenu svako 30 sekundi, otvaranje-zatvaranje, otkljucavanje-zakljucavanje, 
dvaju vrata te klima uredjaj
*/

public class SmartHouseSimulator extends JFrame {

    private static SmartHouseSimulator SmartHome;
    private FirebaseOptions options;
    private final Timer timer;
    private PanelClimate climate1; //panel za klimu
    private PanelDoor door1; //panel za vrata
    private PanelDoor door2;
    private String[] doorDevices; //niz vrata preko kojega saljemo random vrata za open/unlock
    private Random rand;
    private Timestamp timestamp;

    SmartHouseSimulator() throws Exception {
        super();
        super.setLayout(new GridLayout(0, 3)); //postavljanje grida na frame

        doorDevices = new String[]{"HomeDoor1", "HomeDoorMain"}; //niz vrata za slucajni odabir open/unlock
        rand = new Random();

        door1 = new PanelDoor();
        door2 = new PanelDoor();
        climate1 = new PanelClimate();

       
        options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream("smarthouse-6da8f-firebase-adminsdk-ngxia-23ab499cb1.json"))
                .setDatabaseUrl("https://smarthouse-6da8f.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SmartHomeLabJava");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                for (DataSnapshot child : ds.getChildren()) {
                    System.out.println(child.getKey());
                    switch (child.getKey()) {
                        case "Climates":
                            for (DataSnapshot c : child.getChildren()) {
                                if (c.getValue().equals("HomeClimate1")) {
                                    climate1.setTitle(c.getKey()); //header panela
                                }
                            }
                            break;
                        case "Doors":
                            for (DataSnapshot hd : child.getChildren()) {
                                if (hd.getValue().equals("HomeDoor1")) {
                                    door1.setTitle(hd.getKey()); //header panela
                                } else if (hd.getValue().equals("HomeDoorMain")) {
                                    door2.setTitle(hd.getKey());
                                }
                            }
                            break;
                        case "name":
                            SmartHome.setTitle(child.getValue().toString()); //naslov frame-a
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError de) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("HomeClimate1");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {

                //imamo jednostavnu klasu CLimates sa svim varijablama kao u bazi
                Climates HomeClimate1 = ds.getValue(Climates.class); 

                //promjena/postavljanje temperature i vlaznosti nakon sto se promjeni u bazi
                climate1.setLabelHumindity("Huminidity: " + HomeClimate1.getHuminidity());
                climate1.setLabelTemperature("Temperature: " + HomeClimate1.getTemperature());
            }

            @Override
            public void onCancelled(DatabaseError de) {
                // TODO Auto-generated method stub
            }
        });

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("HomeDoor1");

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                for (DataSnapshot child : ds.getChildren()) {
                    switch (child.getKey()) {
                        case "Cmd":
                            for (DataSnapshot cmd : child.getChildren()) {
                                System.out.println(cmd.getKey() + " " + cmd.getValue());
                            }
                            break;
                        case "Keys":
                            System.out.println(child.getValue());
                            break;
                        case "LogDoorOpening":
                            for (DataSnapshot log : child.getChildren()) {
                                String LogDoorOpeningID = log.getKey(); //zadnji uniqueID ce bit postavljen na ekran
                                for (DataSnapshot l : log.getChildren()) {
                                    System.out.println(LogDoorOpeningID);
                                    if (l.getKey().equals("opened")) {
                                        door1.setDoorOpened(true); //ako je pod uniqueID zadnja vrijednost opened
                                    } else {
                                        door1.setDoorOpened(false); //ako je pod uniqueID zadnja vrijednost closed
                                    }
                                    System.out.println(l.getKey() + " " + l.getValue());
                                }
                            }
                            System.out.println();
                            break;
                        case "LogDoorUnlocking":
                            for (DataSnapshot log : child.getChildren()) {
                                String LogDoorUnlockingID = log.getKey();
                                System.out.println(LogDoorUnlockingID);
                                for (DataSnapshot l : log.getChildren()) {
                                    System.out.println(l.getKey() + " " + l.getValue());
                                    if (l.getKey().equals("unlocked")) {
                                        door1.setDoorUnlocked(true);
                                    } else {
                                        door1.setDoorUnlocked(false);
                                    }
                                }
                            }
                            break;
                        case "resetCount":
                            System.out.println(child.getKey() + " " + child.getValue());
                            break;
                        default:
                            break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError de) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("HomeDoorMain");

        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                //System.out.println(ds.getValue());
                for (DataSnapshot child : ds.getChildren()) {
                    if (child.getKey().equals("Cmd")) {
                        for (DataSnapshot cmd : child.getChildren()) {
                            System.out.println(cmd.getKey() + " " + cmd.getValue());
                        }
                    } else if (child.getKey().equals("Keys")) {
                        System.out.println(child.getValue());
                    } else if (child.getKey().equals("LogDoorOpening")) {
                        for (DataSnapshot log : child.getChildren()) {
                            String LogDoorOpeningID = log.getKey();
                            for (DataSnapshot l : log.getChildren()) {
                                System.out.println(LogDoorOpeningID);
                                if (l.getKey().equals("opened")) {
                                    door2.setDoorOpened(true);
                                } else {
                                    door2.setDoorOpened(false);
                                }
                                System.out.println(l.getKey() + " " + l.getValue());
                            }
                        }
                        System.out.println();
                    } else if (child.getKey().equals("LogDoorUnlocking")) {
                        for (DataSnapshot log : child.getChildren()) {
                            String LogDoorUnlockingID = log.getKey();
                            System.out.println(LogDoorUnlockingID);
                            for (DataSnapshot l : log.getChildren()) {
                                System.out.println(l.getKey() + " " + l.getValue());
                                if (l.getKey().equals("unlocked")) {
                                    door2.setDoorUnlocked(true);
                                } else {
                                    door2.setDoorUnlocked(false);
                                }
                            }
                        }
                    } else if (child.getKey().equals("resetCount")) {
                        System.out.println(child.getKey() + " " + child.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError de) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    
                    //iz niza sa nazivima vrata vadimo slucajni i saljemo u metode za promjenu vrijednosti
                    String randomOpen = doorDevices[rand.nextInt(2)];
                    String randomUnlock = doorDevices[rand.nextInt(2)];

                    //metode za izmjenu realtime baze
                    doorOpening(randomOpen);
                    doorUnlocking(randomUnlock);
                    changeTemperature();
                    changeHuminidity();

                } catch (InterruptedException ex) {
                    Logger.getLogger(SmartHouseSimulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 15000); //svako 30 sekunda se poziva

        super.add(this.climate1);
        super.add(this.door1);
        super.add(this.door2);

        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(600, 500);
        super.setLocationRelativeTo(null); //center
        super.setResizable(true);
        super.setVisible(true);
    }

    public void doorOpening(String randomOpen) throws InterruptedException {
        //vrijednost closed
        Map<String, Long> closeDoor = new HashMap<>();
        timestamp = new Timestamp(System.currentTimeMillis()); //vremenski generator za open/unlock
        closeDoor.put("closed", timestamp.getTime()); //generiramo timestamp za zapis u bazu
        //referenca je naziv vrata koji metoda prima slucajnim odabirom
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(randomOpen); 

        String LogDoorOpeningID = ref.child("LogDoorOpening").push().getKey(); //pamti uniqueID i tu dodajemo open/closed
        ref.child("LogDoorOpening").child(LogDoorOpeningID).setValue(closeDoor);

        Thread.sleep(7000); //cekanje 7 sekundi prije open

        Map<String, Object> openDoor = new HashMap<>();
        openDoor.put("opened", timestamp.getTime());
        //pomocu uniqueID-a dodajemo opened na isto mjesto gdje smo prethodno zatvorili
        ref.child("LogDoorOpening").child(LogDoorOpeningID).updateChildren(openDoor);

    }

    public void doorUnlocking(String randomUnlock) throws InterruptedException {
        Map<String, Long> lockDoor = new HashMap<>();
        timestamp = new Timestamp(System.currentTimeMillis()); 
        lockDoor.put("locked", timestamp.getTime());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(randomUnlock);

        String LogDoorOpeningID = ref.child("LogDoorOpening").push().getKey();
        ref.child("LogDoorUnlocking").child(LogDoorOpeningID).setValue(lockDoor);

        Thread.sleep(7000);

        Map<String, Object> unlockDoor = new HashMap<>();
        unlockDoor.put("unlocked", timestamp.getTime());
        ref.child("LogDoorUnlocking").child(LogDoorOpeningID).updateChildren(unlockDoor);

    }
    //promjena temperature
    public void changeTemperature() {
        /*rand.nextInt((max - min) + 1) + min;*/
        int randTemp = rand.nextInt((40 - 15) + 1) + 15;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("HomeClimate1");
        ref.child("temperature").setValue(randTemp);
    }
    //promjena vlaznosti
    public void changeHuminidity() {
        /*rand.nextInt((max - min) + 1) + min;*/
        int randTemp = rand.nextInt((70 - 20) + 1) + 20;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("HomeClimate1");
        ref.child("huminidity").setValue(randTemp);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SmartHome = new SmartHouseSimulator();
                } catch (Exception ex) {
                    Logger.getLogger(SmartHouseSimulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
