package com.technototes.library.command;

import com.technototes.library.command.Command;
import com.technototes.library.command.CommandGroup;

import java.util.Map;

/** Command group to run commands in sequence
 * @author Alex Stedman
 */
public class SequentialCommandGroup extends CommandGroup {
    protected Command lastCommand;

    /** Make sequential command group
     *
     * @param commands The commands to run
     */
    public SequentialCommandGroup(Command... commands) {
        super(commands);
    }


    @Override
    public void schedule(Command c) {
        if(lastCommand == null){
            this.with(c);
        } else{
            lastCommand.then(c);
        }
        lastCommand = c;
    }

    /** Returns if all the commands are finished
     *
     * @return Is the command group finished
     */
    @Override
    public boolean isFinished() {
        return !commandMap.containsValue(false);
    }

    /*
    g
    ga
    ga
    ga
    ga
    gab
    g b
    g b
    g bc
    g  c
    g  c
    g  c
    g

     */

}
