package org.firstinspires.ftc.teamcode.commands.index;

import com.technototes.library.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.IndexSubsystem;

public class IndexPivotMidCommand extends WaitCommand {
    public IndexSubsystem indexSubsystem;
    public IndexPivotMidCommand(IndexSubsystem subsystem){
        //COOLDOWN
        super(0.1);
        //addRequirements(subsystem);
        indexSubsystem = subsystem;
    }

    @Override
    public void execute() {
        indexSubsystem.pivot.setPosition(0.6);
    }
}
