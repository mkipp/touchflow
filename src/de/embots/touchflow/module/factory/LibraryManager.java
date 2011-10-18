package de.embots.touchflow.module.factory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.implementation.input.Amplitude;
import de.embots.touchflow.module.implementation.input.Const;
import de.embots.touchflow.module.implementation.input.Const2D;
import de.embots.touchflow.module.implementation.input.Const3D;
import de.embots.touchflow.module.implementation.input.Keyboard;
import de.embots.touchflow.module.implementation.input.KinectInput3D;
import de.embots.touchflow.module.implementation.input.KinectSimulator3D;
import de.embots.touchflow.module.implementation.input.Mouse2D;
import de.embots.touchflow.module.implementation.input.Pitch;
import de.embots.touchflow.module.implementation.modify.Add;
import de.embots.touchflow.module.implementation.modify.And;
import de.embots.touchflow.module.implementation.modify.Angle2D;
import de.embots.touchflow.module.implementation.modify.BandFilter;
import de.embots.touchflow.module.implementation.modify.CircleProjection;
import de.embots.touchflow.module.implementation.modify.Distance2D;
import de.embots.touchflow.module.implementation.modify.Distance3D;
import de.embots.touchflow.module.implementation.modify.InputSelect;
import de.embots.touchflow.module.implementation.modify.InputSelect2D;
import de.embots.touchflow.module.implementation.modify.InputSelect3D;
import de.embots.touchflow.module.implementation.modify.IntervalMap;
import de.embots.touchflow.module.implementation.modify.IsStable;
import de.embots.touchflow.module.implementation.modify.AffineMap;
import de.embots.touchflow.module.implementation.modify.MapPosCircle2D;
import de.embots.touchflow.module.implementation.modify.MapPosSquare2D;
import de.embots.touchflow.module.implementation.modify.MultiAffineMap;
import de.embots.touchflow.module.implementation.modify.Multiply;
import de.embots.touchflow.module.implementation.modify.Multiply2D;
import de.embots.touchflow.module.implementation.modify.Multiply3D;
import de.embots.touchflow.module.implementation.modify.OutputSelect;
import de.embots.touchflow.module.implementation.modify.Relativator;
import de.embots.touchflow.module.implementation.modify.Relativator3D;
import de.embots.touchflow.module.implementation.modify.StabilityFilter2D;
import de.embots.touchflow.module.implementation.modify.Warp;
import de.embots.touchflow.module.implementation.modify._2DTo1D;
import de.embots.touchflow.module.implementation.output.CharAnimOut;
import de.embots.touchflow.module.implementation.output.FormattedOut;
import de.embots.touchflow.module.implementation.output.Print;
import de.embots.touchflow.module.implementation.output.Print2D;
import de.embots.touchflow.module.implementation.output.Socket;
import de.embots.touchflow.module.implementation.output.Socket2D;
import de.embots.touchflow.module.implementation.output.StatPrint;
import de.embots.touchflow.util.RAClass;

public class LibraryManager
{

    ArrayList<Class<InputModule>> inputModules = new ArrayList<Class<InputModule>>();
    ArrayList<Class<ModifyModule>> modifyModules = new ArrayList<Class<ModifyModule>>();
    ArrayList<Class<OutputModule>> outputModules = new ArrayList<Class<OutputModule>>();
    HashMap<String, Class> modules = new HashMap<String, Class>();
    HashMap<String, String> modNameToClassName = new HashMap<String, String>();
    public static LibraryManager manager = new LibraryManager();

    static {
        if (!Globals.isJar) {
            //internal modules
            manager.addPackage("module.implementation.input");
            manager.addPackage("module.implementation.modify");
            manager.addPackage("module.implementation.output");
        } else {
            //jar: register modules manually

            try {
                manager.registerModule(new Mouse2D());
                manager.registerModule(new Amplitude());
                manager.registerModule(new Const());
                manager.registerModule(new Const2D());
                manager.registerModule(new Const3D());
                manager.registerModule(new Keyboard());
                manager.registerModule(new Pitch());
                //manager.registerModule(new TUIOFinger2D());
                manager.registerModule(new Angle2D());
                manager.registerModule(new BandFilter());
                
                manager.registerModule(new Distance2D());
                manager.registerModule(new StabilityFilter2D());
                manager.registerModule(new MapPosCircle2D());
                manager.registerModule(new MapPosSquare2D());
                manager.registerModule(new Multiply());
                manager.registerModule(new Multiply2D());
                manager.registerModule(new InputSelect());
                manager.registerModule(new InputSelect2D());
                manager.registerModule(new InputSelect3D());
                manager.registerModule(new OutputSelect());
                manager.registerModule(new Relativator());
                manager.registerModule(new Add());
                manager.registerModule(new _2DTo1D());
                manager.registerModule(new Warp());
                manager.registerModule(new FormattedOut());
                manager.registerModule(new Print2D());
                manager.registerModule(new Print());
                manager.registerModule(new Socket());
                manager.registerModule(new Socket2D());
                manager.registerModule(new StatPrint());
                manager.registerModule(new KinectInput3D());
                manager.registerModule(new KinectSimulator3D());
                manager.registerModule(new Distance3D());
                manager.registerModule(new Relativator3D());
                manager.registerModule(new CharAnimOut());
                manager.registerModule(new AffineMap());
                manager.registerModule(new IntervalMap());
                manager.registerModule(new IsStable());
                manager.registerModule(new And());
                manager.registerModule(new Multiply3D());
                manager.registerModule(new MultiAffineMap());
                manager.registerModule(new CircleProjection());
                
                //"Fehler beim laden"
            } catch (ModulException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String... args) throws ModulFactoryException
    {
        LibraryManager man = new LibraryManager();

        man.addPackage("module.implementation.input");
        man.addPackage("module.implementation.modify");
        man.addPackage("module.implementation.output");


    }

    public ArrayList<Class<InputModule>> getInputModules()
    {
        return inputModules;
    }

    public ArrayList<Class<ModifyModule>> getModifyModules()
    {
        return modifyModules;
    }

    public ArrayList<Class<OutputModule>> getOutputModules()
    {
        return outputModules;
    }

    public Module getParameterizedInstance(String moduleName, String constructorParams) throws ModulException
    {

        Module ret = getInstance(moduleName);

        ret.init(constructorParams);

        return ret;
    }

    public Module getInstance(String moduleName) throws ModulFactoryException
    {
        Class c = modules.get(moduleName);
        if (c == null) {
            throw new ModulFactoryException("LibMan: getInstance: Class " + moduleName + " is not registered");
        }

        Constructor constr;

        //find constructor
        try {
            constr = c.getConstructor(new Class[0]);
        } catch (SecurityException e) {
            throw new ModulFactoryException("LibMan: getInstance: SecurityException creating " + moduleName);
        } catch (NoSuchMethodException e) {
            throw new ModulFactoryException("LibMan: getInstance: Class " + moduleName + " has no default constructor");
        }

        Object ret;

        try {
            ret = constr.newInstance(new Class[0]);
        } catch (Exception e) {
            throw new ModulFactoryException("LibMan: getInstance: Class " + moduleName + " could not be constructed:\n" + e + " caused by\n" + getStackTrace(e.getCause()));

        }

        if (!(ret instanceof Module)) {
            throw new ModulFactoryException("LibMan: getInstance: Class " + moduleName + " returned wrong type:" + ret.getClass());
        }
        return (Module) ret;

    }

    private String getStackTrace(Throwable cause)
    {
        String ret = cause.toString() + "\n";

        for (StackTraceElement el : cause.getStackTrace()) {
            ret = ret + el + "\n";
        }
        return ret;
    }

    /**
     * @deprecated Will no longer work, as modNameToClassName is not updated!
     */
    public void addPackage(String paket)
    {
        try {


            for (Class c : getClasses(paket, InputModule.class)) {
                modules.put(c.getSimpleName(), c);

                inputModules.add(c);
                if (Globals.isDebug) {
                    System.out.println("found module:" + c.getSimpleName());
                }
            }
            for (Class c : getClasses(paket, ModifyModule.class)) {
                modules.put(c.getSimpleName(), c);
                modifyModules.add(c);
                System.out.println("found module:" + c.getSimpleName());
            }
            for (Class c : getClasses(paket, OutputModule.class)) {
                modules.put(c.getSimpleName(), c);
                outputModules.add(c);
                System.out.println("found module:" + c.getSimpleName());
            }



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void registerModule(Module m)
    {
        modules.put(m.getClass().getSimpleName(), m.getClass());
        modNameToClassName.put(m.getModuleName(), m.getClass().getSimpleName());

        if (m instanceof InputModule) {
            inputModules.add((Class<InputModule>) m.getClass());
        }
        if (m instanceof ModifyModule) {
            modifyModules.add((Class<ModifyModule>) m.getClass());
        }
        if (m instanceof OutputModule) {
            outputModules.add((Class<OutputModule>) m.getClass());
        }
    }

    /**
     * Modul aus XML-Block erstellen
     * @param entry XML-Block als ArrayList der zugehörigen Zeilen
     * @return
     * @throws ModulException 
     */
    public Module createModulFromEntry(Element e) throws ModulException
    {

        int id = parseID(e);
//	
        if (e.getAttribute("type") == null) {
            throw new ModulFactoryException("Attribut 'type' fehlt bei Modul " + id);
        }




        String type = e.getAttribute("type").getValue();
        String konstrParams = null;
        if (e.getAttribute("Constructor") != null) {
            konstrParams = e.getAttribute("Constructor").getValue();
        }
        Module m = getParameterizedInstance(type, konstrParams);

        if (e.getAttribute("graphXPos") != null) {
            if (e.getAttribute("graphYPos") == null) {
                throw new ModulFactoryException("Attribut 'graphYPos' fehlt, obwohl X-Koordinate angegeben wurde bei Modul " + id);
            }
            int xpos, ypos;

            try {
                xpos = Integer.parseInt(e.getAttribute("graphXPos").getValue());
                ypos = Integer.parseInt(e.getAttribute("graphYPos").getValue());

            } catch (NumberFormatException nf) {
                throw new ModulFactoryException("XPos oder YPos ist kein int bei Modul " + id);
            }

            m.setGraphXPos(xpos);
            m.setGraphYPos(ypos);
        }

        m.setId(id);

        if (maxModulID < id) {
            maxModulID = id;
        }

        return m;




    }
//merkt sich beim laden die höchste id, um diese an Modul weiterzugeben, 
//damit die nächsten Module die erstellt werden eine richtige id haben (auto increment)
    private static int maxModulID = 0;

    public static int getMaxModulID()
    {
        return maxModulID;
    }

    private static int parseID(Element e) throws ModulFactoryException
    {
        int id;

        if (e.getAttribute("id") == null) {
            throw new ModulFactoryException("Attribut 'id' fehlt");
        }

        try {
            id = Integer.parseInt(e.getAttribute("id").getValue());
        } catch (NumberFormatException nf) {
            throw new ModulFactoryException("Attribut 'id' ist kein int");
        }
        return id;
    }

//##################### hacky section - think and replace this #####################
    /**
     * erstellt ein Modul für den Graphen. Fetcht eventuell nötige Konstruktorparameter per GUI
     * @param type
     * @throws ModulException 
     */
    public Module createModulForGraph(String type) throws ModulException
    {



        String params = "";

        if (type.equals("MapPos Circle 2D") || type.equals("MapPos Square 2D")) {
            params = "100 100 50";

            String points = JOptionPane.showInputDialog("Bitte Anzahl der Peripheriepunkte eingeben");
            if (params == null) {
                throw new ModulException("keine params");
            }
            checkInt(points);

            if (type.equals("MapPos Square 2D") && !points.equals("4") && !points.equals("8")) {
                RAClass.msgbox("Anzahl an Peripheriepunkten muss 4 oder 8 sein", "Error", "Warning");
                return null;
            }
            params = params + " " + points;

            String centerint = JOptionPane.showInputDialog("Soll ein Mittelpunkt verwendet werden? Geben Sie 0 fuer 'nein', und eine andere Zahl fuer 'ja' ein.");
            if (params == null) {
                throw new ModulException("keine params");
            }
            checkInt(centerint);
            params = params + " " + centerint;

            // nicht mehr bei erstellung abfragen, einfach mit standartwerten initialisieren

            String className = modNameToClassName.get(type);

            if (className == null) {
                throw new ModulException("Modul named '" + type + "' is not registered");
            }

            return getParameterizedInstance(className, params);
        }



        // nicht mehr bei erstellung abfragen, einfach mit standartwerten initialisieren

        String className = modNameToClassName.get(type);

        if (className == null) {
            throw new ModulException("Modul named '" + type + "' is not registered");
        }

        return LibraryManager.manager.getInstance(className);
    }

    /**
     * prueft ob es ein int ist; falls nicht wird exception geworfen
     * @param zahl
     * @throws ModulFactoryException
     */
    private static void checkInt(String zahl) throws ModulFactoryException
    {
        try {
            Integer.parseInt(zahl);
        } catch (NumberFormatException nf) {
            RAClass.msgbox("Eingabe muss eine Integer-Ganzzahl sein!", "TouchFlow - GraphTool", "Warning");
            throw new ModulFactoryException("String kein int");
        }

    }

//##################### internal section ###########################
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static ArrayList<Class> getClasses(String packageName, Class baseClass)
            throws ClassNotFoundException, IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }

        //filter wrong classes

        if (baseClass != null) {
            ArrayList<Class> toRemove = new ArrayList<Class>();

            for (Class c : classes) {

                if (!inheritsFrom(c, baseClass)) {
                    toRemove.add(c);
                }
            }

            classes.removeAll(toRemove);
        }

        return classes;
    }

    private static boolean inheritsFrom(Class c, Class superclass)
    {
        if (c.getSuperclass() == null) {
            return false;
        }
        if (c.getSuperclass() == superclass) {
            return true;
        }
        return inheritsFrom(c.getSuperclass(), superclass);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException
    {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
