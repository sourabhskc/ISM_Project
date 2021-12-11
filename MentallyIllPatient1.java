import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.*;
import java.sql.*;
public class MentallyIllPatient1 extends JFrame implements MouseListener
{
	
	JLabel lab,lab1,lbut,limg,clo;
	JButton bat1,bat,cbut;
	MentallyIllPatient1()
	{
		setLayout(null);
		comPonent();
		setUndecorated(true);
		setSize(1150,700);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	void comPonent()
	{
		setLayout(null);
		Font f=new Font("hf",Font.BOLD,75);
		lab = new JLabel("MENTIALLY ILL PATIENT");
		add(lab);
		lab.setFont(f);
		lab.setForeground(Color.YELLOW);
		lab.setBounds(150,25,1000,80);

		// lab = new JLabel(new ImageIcon("download.png"));
		// add(lab);
		// lab.setBounds(450,100,400,300);
		clo = new JLabel(new ImageIcon("lib.png"));
		add(clo);
		clo.setBounds(1090,10,30,30);
		clo.addMouseListener(this);
		lbut = new JLabel(new ImageIcon("UohBut.png"));
		add(lbut);
		lbut.setBounds(300,200,450,40);
		lbut.addMouseListener(this);
		bat = new JButton("rec.");
		add(bat);
		bat.setBounds(700,210,35,30);
		bat.setBackground(Color.green);
		// bat.addMouseListener(this);
		bat1 = new JButton("stop");
		add(bat1);
		bat1.setBounds(700,210,35,30);
		bat1.setBackground(Color.red);
		// bat1.addMouseListener(this);
		bat.setVisible(false);
		bat1.setVisible(false);
		limg = new JLabel(new ImageIcon("ill1.jpeg"));
		add(limg);
		limg.setBounds(0,0,1150,700);

	}
	public void mouseClicked(MouseEvent e1)
	{
		if(e1.getSource()== lbut)
		{
			try
			{
				
				AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,2,4,44100,false);

				DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class,audioFormat);
				if(!AudioSystem.isLineSupported(dataInfo))
				{
					System.out.println("Not Supported");
				}

				TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(dataInfo);
				targetLine.open();
				JOptionPane.showMessageDialog(null,"Hit ok to start recording...");
				bat.setVisible(true);
				bat1.setVisible(false);
				targetLine.start();

				Thread audioRecorderThread = new Thread()
				{
					@Override public void run()
					{
						AudioInputStream recordingStream = new AudioInputStream(targetLine);
						
						File outputFile = new File("E:record.Wav");
						try
						{
							AudioSystem.write(recordingStream,AudioFileFormat.Type.WAVE,outputFile);
						}
						catch(IOException ex)
						{
							System.out.println(ex);
						}
						try
						{
							Class.forName("com.mysql.cj.jdbc.Driver");
							String url="jdbc:mysql://localhost:3306/illpatient?autoReconnect=true&useSSL=false";
							String username="root";
							String password="root";
							Connection con=DriverManager.getConnection(url,username,password);
							String q="insert into audiodata(audio) values(?)";
							PreparedStatement st=con.prepareStatement(q);
							
							FileInputStream fi=new FileInputStream("E:record5.Wav");
							st.setBinaryStream(1,fi,fi.available());
							st.executeUpdate();
							System.out.println("done...");
							con.close();
							st.close();

						}
						catch(Exception e1)
						{
							System.out.println(e1);
						}
						System.out.println("stoped recording..");
					}
				};

				audioRecorderThread.start();
				JOptionPane.showMessageDialog(null,"Hit Ok and stop recording.");
				bat1.setVisible(true);
				bat.setVisible(false);
				targetLine.stop();
				targetLine.close();

			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		if(e1.getSource() == clo)
		{
			System.exit(0);
		}

	}
	public void mouseEntered(MouseEvent e2)
	{}
	public void mouseExited(MouseEvent e3)
	{}
	public void mousePressed(MouseEvent e4)
	{}
	public void mouseReleased(MouseEvent e5)
	{}

	public static void main(String[] args)
	{
		new MentallyIllPatient1();
	}
}