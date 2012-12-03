/**
 * MappingTest.java
 */

package com.beardediris.ajaqs.db;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.util.Logger;

public class MappingTest implements Constants
{
    private final static String DBFILE = "database.xml";
    private final static String MAPFILE = "mapping.xml";
    private final static String PROJECT = "Ajaqs-Reloaded";
    private final static String ROLE = "ajaqs-guest";
    private final static String QUESTION = "Who wrote Ajaqs and why?";
    private final static String ANSWER =
        "Charlie wrote it in order to help engineers.";

    private String separator = null;
    private Database db;
    private PrintWriter writer;

    private void setSeparator() {
        try {
            separator = System.getProperty("file.separator");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private String mapFile(String dir) {
        if (null == separator) {
            setSeparator();
        }
        StringBuffer sb = new StringBuffer(dir);
        sb.append(separator);
        sb.append(MAPFILE);
        return sb.toString();
    }

    private String dbFile(String dir) {
        if (null == separator) {
            setSeparator();
        }
        StringBuffer sb = new StringBuffer(dir);
        sb.append(separator);
        sb.append(DBFILE);
        return sb.toString();
    }

    private String encrypt(String text)
        throws Exception
    {
        PBEParameterSpec pbeParamSpec;
        PBEKeySpec pbeKeySpec;
        SecretKeyFactory keyFac;
        SecretKey pbeKey;

        int textlen = text.length();
        char[] cleartext = new char[textlen];
        text.getChars(0, textlen, cleartext, 0);

        // Salt
        byte[] salt = {
            (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
            (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
        };

        // Iteration count
        int count = 20;

        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);
        pbeKeySpec = new PBEKeySpec(cleartext);
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        pbeKey = keyFac.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

        // Our cleartext
        byte[] cleartextBytes = text.getBytes();

        // Encrypt the cleartext
        byte[] ciphertextBytes = pbeCipher.doFinal(cleartextBytes);
        String retval = new String(ciphertextBytes);

        return retval;
    }

    public void testProject()
        throws Exception
    {
        db.begin();

        Project proj = new Project();
        proj.setName(PROJECT);
        proj.setCreationDate(new Date());
        proj.setState(Project.ACTIVE);
        db.create(proj);
        writer.println("Created new Project: " + proj);

        db.commit();
    }

    public void testRole()
        throws Exception
    {
        db.begin();

        Role role = new Role();
        role.setRoleName(ROLE);
        db.create(role);
        writer.println("Created new Role: " + role);

        db.commit();
    }

    public void testFaqUser()
        throws Exception
    {
        db.begin();

        Project proj = null;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT p FROM com.beardediris.ajaqs.db.Project p ");
        sb.append("WHERE p.name = $1");

        OQLQuery oql = db.getOQLQuery(sb.toString());
        oql.bind(PROJECT);

        QueryResults results = oql.execute();
        if (results.hasMore()) {
            proj = (Project)results.next();
        } else {
            throw new RuntimeException("Missing Project");
        }

        ArrayList projects = new ArrayList();
        projects.add(proj);

        Role role = null;
        sb.setLength(0);
        sb.append("SELECT r FROM com.beardediris.ajaqs.db.Role r ");
        sb.append("WHERE r.roleName = $1");

        oql = db.getOQLQuery(sb.toString());
        oql.bind(ROLE);

        results = oql.execute();
        if (results.hasMore()) {
            role = (Role)results.next();
        } else {
            throw new RuntimeException("Missing Role");
        }

        ArrayList roles = new ArrayList();
        roles.add(role);

        FaqUser fuser = new FaqUser();
        fuser.setLogon("salome");
        fuser.setPassword(encrypt("salome123"));
        fuser.setEmail("salome@hotmail.com");
        fuser.setCreationDate(new Date());
        fuser.setLastLoginDate(new Date());
        fuser.setState(FaqUser.ACTIVE);
        fuser.setProjects(projects);
        fuser.setRoles(roles);
        db.create(fuser);
        writer.println("Created new FaqUser: " + fuser);

        db.commit();
    }

    public void testFaq()
        throws Exception
    {
        db.begin();

        // Lookup project.
        Project proj = null;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT p FROM com.beardediris.ajaqs.db.Project p ");
        sb.append("WHERE p.name = $1");

        OQLQuery projOql = db.getOQLQuery(sb.toString());
        projOql.bind(PROJECT);

        QueryResults results = projOql.execute();
        if (results.hasMore()) {
            proj = (Project)results.next();
        } else {
            throw new RuntimeException("Missing Project");
        }

        Faq faq = new Faq();
        faq.setName("Origins");
        faq.setCreationDate(new Date());
        faq.setState(Faq.ACTIVE);
        faq.setProject(proj);
        db.create(faq);
        writer.println("Created new Faq: " + faq);

        db.commit();
    }

    public void testQuestion()
        throws Exception
    {
        db.begin();

        Faq faq = null;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT f FROM com.beardediris.ajaqs.db.Faq f ");
        sb.append("WHERE f.name = $1");

        OQLQuery oql = db.getOQLQuery(sb.toString());
        oql.bind("Origins");

        QueryResults results = oql.execute();
        if (results.hasMore()) {
            faq = (Faq)results.next();
        } else {
            throw new RuntimeException("Missing Faq");
        }

        FaqUser fuser = null;
        sb.setLength(0);
        sb.append("SELECT u FROM com.beardediris.ajaqs.db.FaqUser u ");
        sb.append("WHERE u.logon = $1");

        oql = db.getOQLQuery(sb.toString());
        oql.bind("salome");
        results = oql.execute();
        if (results.hasMore()) {
            fuser = (FaqUser)results.next();
        } else {
            throw new RuntimeException("Missing FaqUser");
        }

        Question question = new Question();
        question.setQuestion(QUESTION);
        question.setCreationDate(new Date());
        question.setFaq(faq);
        question.setUser(fuser);
        db.create(question);
        writer.println("Created new Question: " + question);

        db.commit();
    }

    public void testAnswer()
        throws Exception
    {
        db.begin();

        Question question = null;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT q FROM com.beardediris.ajaqs.db.Question q ");
        sb.append("WHERE q.question = $1");

        OQLQuery oql = db.getOQLQuery(sb.toString());
        oql.bind(QUESTION);

        QueryResults results = oql.execute();
        if (results.hasMore()) {
            question = (Question)results.next();
        } else {
            throw new RuntimeException("Missing Question");
        }

        FaqUser fuser = null;
        sb.setLength(0);
        sb.append("SELECT u FROM com.beardediris.ajaqs.db.FaqUser u ");
        sb.append("WHERE u.logon = $1");

        oql = db.getOQLQuery(sb.toString());
        oql.bind("salome");
        results = oql.execute();
        if (results.hasMore()) {
            fuser = (FaqUser)results.next();
        } else {
            throw new RuntimeException("Missing FaqUser");
        }

        Answer answer = new Answer();
        answer.setAnswer(ANSWER);
        answer.setCreationDate(new Date());
        answer.setQuestion(question);
        answer.setUser(fuser);
        db.create(answer);
        writer.println("Created new Answer: " + answer);

        db.commit();
    }

    public void testAttachment()
        throws Exception
    {
        db.begin();

        Answer answer = null;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT a FROM com.beardediris.ajaqs.db.Answer a ");
        sb.append("WHERE a.answer = $1");

        OQLQuery oql = db.getOQLQuery(sb.toString());
        oql.bind(ANSWER);

        QueryResults results = oql.execute();
        if (results.hasMore()) {
            answer = (Answer)results.next();
        } else {
            throw new RuntimeException("Missing Answer");
        }

        FaqUser fuser = null;
        sb.setLength(0);
        sb.append("SELECT u FROM com.beardediris.ajaqs.db.FaqUser u ");
        sb.append("WHERE u.logon = $1");

        oql = db.getOQLQuery(sb.toString());
        oql.bind("salome");
        results = oql.execute();
        if (results.hasMore()) {
            fuser = (FaqUser)results.next();
        } else {
            throw new RuntimeException("Missing FaqUser");
        }

        // To create an attachment, we convert a constant
        // String to an array of bytes read onto an InputStream.
        ByteArrayInputStream bais = new ByteArrayInputStream
            ("helloworld - attachment".getBytes());

        Attachment attachment = new Attachment();
        attachment.setAttachment(bais);
        attachment.setDescr("Just a String");
        attachment.setCreationDate(new Date());
        attachment.setFileType("plaintext");
        attachment.setAnswer(answer);
        attachment.setUser(fuser);
        db.create(attachment);
        writer.println("Created new Attachment: " + attachment);

        db.commit();
    }

    public void testWriteTransaction()
        throws Exception
    {
        db.begin();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT u FROM com.beardediris.ajaqs.db.FaqUser u ");
        sb.append("WHERE u.logon = $1");

        FaqUser fuser = null;
        OQLQuery oql = db.getOQLQuery(sb.toString());
        oql.bind("salome");

        QueryResults results = oql.execute();
        results = oql.execute();
        if (results.hasMore()) {
            fuser = (FaqUser)results.next();
        } else {
            throw new RuntimeException("Missing FaqUser");
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 5);
        fuser.setLastLoginDate(cal.getTime());

        db.commit();
    }

    public MappingTest(PrintWriter writer, String xmldir, String dbName)
        throws Exception
    {
        // Use a "file:" prefix for the URL.
        StringBuffer sb = new StringBuffer("file:");
        URL dbspecs = new URL(sb.append(dbFile(xmldir)).toString());
        JDOManager.loadConfiguration(dbspecs.toString());
        JDOManager jdoMgr = JDOManager.createInstance(dbName);

        // Set instance variables.
        this.db = jdoMgr.getDatabase();
        this.writer = writer;
    }

    /**
     * usage: MappingTest xmldir database
     *
     * NOTE the <xmldir> argument should specify the location
     * of DBFILE and MAPFILE files.
     */
    public static void main(String argv[]) {
        Logger writer = new Logger(System.out);
        writer.setPrefix("test");

        if (argv.length != 2) {
            writer.println("usage: MappingTest xmldir database");
            System.exit(-1);
        }

        try {
            MappingTest mt = new MappingTest(writer, argv[0], argv[1]);
            mt.testProject();
            mt.testRole();
            mt.testFaqUser();
            mt.testFaq();
            mt.testQuestion();

            /*
             * These next two lines query blobs, and that leads
             * to exceptions.
            mt.testAnswer();
            mt.testAttachment();
            */

            mt.testWriteTransaction();
        } catch (Exception ex) {
            ex.printStackTrace(writer);
        }

        System.exit(0);
    }
}
