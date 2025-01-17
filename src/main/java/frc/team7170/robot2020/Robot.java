/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team7170.robot2020;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team7170.robot2020.commands.EndEffector;
import frc.team7170.robot2020.commands.TankDrive;
import frc.team7170.robot2020.subsystems.Drivebase;
import frc.team7170.robot2020.commands.ManualElevator;
import frc.team7170.robot2020.commands.ManualFrontArm;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team7170.robot2020.subsystems.Elevator;
import frc.team7170.robot2020.subsystems.FrontArmControl;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CameraServer;
import frc.team7170.robot2020.subsystems.Solnoid;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
// If you rename or move this class, update the build.properties file in the project root
public class Robot extends TimedRobot 
{

    public static void main(String[] args){
        Robot.startRobot(Robot::new);
    }

    //Runs subsystems
    public static final Drivebase drivebase = new Drivebase();
    public static final FrontArmControl FrontArmControl = new FrontArmControl();
    public static final Elevator Elevator = new Elevator();
    public static final Solnoid Solnoid = new Solnoid();
    public static OI oi;
    private final Timer m_timer = new Timer();
    public double value = 1;

    Compressor c = new Compressor(6);



    private Command autonomousCommand;
    private SendableChooser<Command> chooser = new SendableChooser<>();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() 
    {
        oi = new OI();
        chooser.addDefault("Default Auto", new TankDrive());
        chooser.addDefault("Default Auto", new ManualElevator());
        chooser.addDefault("Default Auto", new ManualFrontArm());
        chooser.addDefault("Default Auto", new EndEffector());
        // chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);

        c.enabled();
        CameraServer.getInstance().startAutomaticCapture();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    @Override
    public void disabledInit() 
    {
        
    }

    @Override
    public void disabledPeriodic() 
    {
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString code to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons
     * to the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit()
    {
        autonomousCommand = chooser.getSelected();

        /*
         * String autoSelected = SmartDashboard.getString("Auto Selector",
         * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
         * = new MyAutoCommand(); break; case "Default Auto": default:
         * autonomousCommand = new ExampleCommand(); break; }
         */

        // schedule the autonomous command (example)
        if (autonomousCommand != null) 
        {
            autonomousCommand.start();
        }
        m_timer.start();
        drivebase.set(ControlMode.PercentOutput, 35,35);

    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() 
    {
        if (m_timer.get() > value); {
            drivebase.set(ControlMode.PercentOutput,0,0);
    }
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() 
    {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) 
        {
            autonomousCommand.cancel();
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() 
    {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() 
    {
        
    }

    public static void initTalon(TalonSRX motor){
        motor.setNeutralMode(NeutralMode.Coast);
        motor.setSensorPhase(false);
        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        motor.configNominalOutputForward(0.0 , 0);
        motor.configNominalOutputForward(0.0 , 0);
        motor.configClosedloopRamp(0.25,0);
    }
}
