
#include "program_header.h"

/// @brief Get the time and round it
/// @return (time value)
char	*get_time(void)
{
	time_t now;
	time(&now);

	return (ctime(&now));
}

/**
 * Function will try to open the log file and write the action that was made
 * by the user in the program.
 *
 * @param program program variable
 * @param message interaction of the user with the command line
 * @return (TRUE - if the write went perfectly)
 * @return (FALSE - if the file could not be opened)
 */
int write_log(t_program *program, char *message)
{
	int fd = open(program->log_filename, O_RDWR | O_CREAT | O_APPEND);
	if (fd < 0)
	{
		write(1, "Could not write in log file!", 28);
		return (FALSE);
	}
	else
	{
		// make a assembly function that will get the user's name and append it to the other
		// things, so it can directly write it!
        char *full_line = concatenate(program, message);

        write(fd, full_line, ft_strlen(full_line));

        free(full_line); 
        close(fd);
        //
		//write(fd, program->user->name, ft_strlen(program->user->name));
		//write(fd, ": ", 2);
		//write(fd, message, ft_strlen(message));
		//write(fd, ": ", 2);
		//char *time = get_time();
		//write(fd, time, ft_strlen(time));
		//write(fd, "\n", 1);
		//close(fd);
	}
	return (TRUE);
}

void animate_transit_light(int scenario) {
    printf("\n\n");
    printf(BOLD "┌───────────────────────────────────────────────────────┐\n");
    printf("│" CYAN "               TRANSIT LIGHT STATUS                    " RESET BOLD "│\n");
    printf("└───────────────────────────────────────────────────────┘\n" RESET);

    switch(scenario) {
        case ERROR_RESPONSE: // Clean error screen with scanning effect

                printf(CLEAR_LINE);
                printf("╔══════════════════════════════════════════════════════╗\n");
                printf("║" BRIGHT_RED BOLD "               SYSTEM ERROR DETECTED                  " RESET "║\n");
                printf("╠══════════════════════════════════════════════════════╣\n");
                printf("║                                                      ║\n");
                printf("║" BRIGHT_RED "          ┌────────────────────────────┐              " RESET "║\n");
                printf("║" BRIGHT_RED "          │       ERROR CODE: 404      │              " RESET "║\n");
                printf("║" BRIGHT_RED "          │   COMMAND NOT RECOGNIZED   │              " RESET "║\n");
                printf("║" BRIGHT_RED "          └────────────────────────────┘              " RESET "║\n");
                printf("║                                                      ║\n");
                printf("║                                                      ║\n");
                printf("║                                                      ║\n");
                printf("╚══════════════════════════════════════════════════════╝\n");
                fflush(stdout);
                usleep(400000);

            break;

        case INFORMATION_RECEIVED_RESPONSE: // Card flying to mailbox

            int mailbox_pos = 45; // Position for mailbox
            int total_width = 55; // Total width of the box

            for (int pos = 2; pos < mailbox_pos - 4; pos += 2) {
                printf(CLEAR_LINE);
                printf("╭─────────────────────────────────────────────────────╮\n");
                printf("│" BRIGHT_GREEN BOLD "               📬 DATA TRANSMISSION 📬               " RESET "│\n");
                printf("│                                                     │\n");

                printf("│  ");
                for (int i = 0; i < pos; i++) printf(" ");
                printf(BRIGHT_GREEN "📧➤" RESET);
                for (int i = pos + 3; i < mailbox_pos; i++) printf(" ");
                printf(BRIGHT_GREEN "📮" RESET);
                for (int i = mailbox_pos + 2; i < total_width - 4; i++) printf(" ");
                printf("│\n");

                printf("│  ");
                for (int i = 0; i < pos - 2; i++) {
                    if (i % 4 == 0) printf(BRIGHT_GREEN "|" RESET);
                    else printf(" ");
                }
                printf("   ");
                for (int i = pos + 3; i < mailbox_pos; i++) {
                    if ((i - pos) % 5 == 0) printf(BRIGHT_GREEN "." RESET);
                    else printf(" ");
                }
                printf("   ");
                for (int i = mailbox_pos + 2; i < total_width - 3; i++) printf(" ");
                printf("│\n");

                printf("│                                                     │\n");
                printf("│                                                     │\n");
                printf("│                                                     │\n");
                printf("│                                                     │\n");
                printf("╰─────────────────────────────────────────────────────╯\n");
                fflush(stdout);
                usleep(100000);
            }

            // Mailbox receiving animation
            for (int i = 0; i < 2; i++) {
                printf(CLEAR_LINE);
                printf("╭─────────────────────────────────────────────────────╮\n");
                printf("│" BRIGHT_GREEN BOLD "               📬 DATA TRANSMISSION 📬               " RESET "│\n");
                printf("│                                                     │\n");
                printf("│                                                     │\n");
                printf("│                                                     │\n");

                // Mailbox with card
                printf("│");
                for (int j = 0; j < (total_width - 20)/2; j++) printf(" ");
                if (i % 2 == 0) {
                    printf(BRIGHT_GREEN "📮💌" BRIGHT_GREEN BOLD " DATA RECEIVED!" RESET);
                } else {
                    printf(BRIGHT_GREEN "📮   MAILBOX READY " RESET);
                }
                for (int j = 0; j < (total_width - 20)/2; j++) printf(" ");
                printf("│\n");

                printf("│                                                     │\n");
                printf("│                                                     │\n");
                printf("│                                                     │\n");
                printf("╰─────────────────────────────────────────────────────╯\n");
                fflush(stdout);
                usleep(300000);
            }
            break;

        case GREEN_ON_RESPONSE: // Green transit light
            printf(GREEN);
            for (int i = 0; i < 3; i++) {
                printf(CLEAR_LINE);
                printf("╭─────────────────────────────────────────────────────╮\n");
                printf("│" GREEN BOLD "                  GREEN SIGNAL ACTIVE                " RESET "│\n");
                printf("│                                                     │\n");
                printf("│                   ┌─────────┐                       │\n");
                printf("│                   │    ●    │                       │\n");
                printf("│                   │   ───   │                       │\n");
                printf("│                   │  " GREEN "█████" RESET "  │                       │\n");
                printf("│                   │   ───   │                       │\n");
                printf("│                   │    ●    │                       │\n");
                printf("│                   └─────────┘                       │\n");
                printf("│                    " GREEN BOLD "PROCEED" RESET "                          │\n");
                printf("╰─────────────────────────────────────────────────────╯\n");
                fflush(stdout);
                usleep(500000);
            }
            break;

        case RED_ON_RESPONSE: // Red transit light
            printf(RED);
            for (int i = 0; i < 3; i++) {
                printf(CLEAR_LINE);
                printf("╭─────────────────────────────────────────────────────╮\n");
                printf("│" RED BOLD "                   RED SIGNAL ACTIVE                 " RESET "│\n");
                printf("│                                                     │\n");
                printf("│                   ┌─────────┐                       │\n");
                printf("│                   │  " RED "█████" RESET "  │                       │\n");
                printf("│                   │   ───   │                       │\n");
                printf("│                   │    ●    │                       │\n");
                printf("│                   │   ───   │                       │\n");
                printf("│                   │    ●    │                       │\n");
                printf("│                   └─────────┘                       │\n");
                printf("│                     " RED BOLD "STOP" RESET "                            │\n");
                printf("╰─────────────────────────────────────────────────────╯\n");
                fflush(stdout);
                usleep(500000);
            }
            break;

        case YELLOW_ON_RESPONSE: // Yellow transit light
            printf(YELLOW);
            for (int i = 0; i < 3; i++) {
                printf(CLEAR_LINE);
                printf("╭─────────────────────────────────────────────────────╮\n");
                printf("│" YELLOW BOLD "                 YELLOW SIGNAL ACTIVE                " RESET "│\n");
                printf("│                                                     │\n");
                printf("│                   ┌─────────┐                       │\n");
                printf("│                   │    ●    │                       │\n");
                printf("│                   │   ───   │                       │\n");
                printf("│                   │    ●    │                       │\n");
                printf("│                   │   ───   │                       │\n");
                printf("│                   │  " YELLOW "█████" RESET "  │                       │\n");
                printf("│                   └─────────┘                       │\n");
                printf("│                   " YELLOW BOLD "CAUTION" RESET "                           │\n");
                printf("╰─────────────────────────────────────────────────────╯\n");
                fflush(stdout);
                usleep(500000);
            }
            break;

        case RED_BLINK_RESPONSE: // Blinking red light
            printf(BRIGHT_RED);
            for (int blink = 0; blink < 6; blink++) {
                printf(CLEAR_LINE);
                printf("╭─────────────────────────────────────────────────────╮\n");
                printf("│" BRIGHT_RED BOLD "                BLINKING RED SIGNAL                  " RESET "│\n");
                printf("│                                                     │\n");
                printf("│                   ┌─────────┐                       │\n");
                if (blink % 2 == 0) {
                    printf("│                   │  " BRIGHT_RED "█████" RESET "  │                       │\n");
                } else {
                    printf("│                   │    ●    │                       │\n");
                }
                printf("│                   │   ───   │                       │\n");
                printf("│                   │    ●    │                       │\n");
                printf("│                   │   ───   │                       │\n");
                printf("│                   │    ●    │                       │\n");
                printf("│                   └─────────┘                       │\n");
                if (blink % 2 == 0) {
                    printf("│                 " BRIGHT_RED BOLD "ATTENTION!" RESET "                          │\n");
                } else {
                    printf("│                                                     │\n");
                }
                printf("╰─────────────────────────────────────────────────────╯\n");
                fflush(stdout);
                usleep(300000);
            }
            break;

        // No animation for EXIT_RESPONSE - it should just exit the program
    }
    printf(RESET);
}

void write_funny_response(int which)
{
    printf(CLEAR);

    printf(BOLD "\n╔══════════════════════════════════════════════════════════╗\n");
    printf("║" MAGENTA "                     TRAIN CONTROL CENTER" RESET BOLD "                 ║\n");
    printf("╚══════════════════════════════════════════════════════════╝\n\n" RESET);

    switch(which)
    {
        case ERROR_RESPONSE:
            printf(BRIGHT_RED BOLD "🚫 CHOO-CHOO-ERROR! 🚫\n\n" RESET);
            printf(RED "╰─▸ The conductor is confused!\n");
            printf("╰─▸ That command doesn't exist on this railway.\n");
            printf("╰─▸ Try something like: 'GTH', 'GE', 'RE', 'YE', 'RB', or 'Exit'\n\n");
            printf("💡 Tip: All aboard the syntax express!\n" RESET);
            break;

        case INFORMATION_RECEIVED_RESPONSE:
            printf(GREEN BOLD "📬 MESSAGE INCOMING! 📬\n\n" RESET);
            printf(GREEN "╰─▸ Signal received! The telegram has arrived!\n");
            printf("╰─▸ Station master says: \"GTH acknowledged!\"\n");
            printf("╰─▸ Train to Informationville is now boarding!\n\n");
            break;

        case GREEN_ON_RESPONSE:
            printf(GREEN BOLD "🟢 ALL CLEAR! PROCEED! 🟢\n\n" RESET);
            printf(GREEN "╰─▸ Green light! FULL STEAM AHEAD! 🚂\n");
            printf("╰─▸ Track is clear\n");
            printf("╰─▸ Conductors are smiling (rare sight!)\n\n");
            break;

        case RED_ON_RESPONSE:
            printf(RED BOLD "🛑 HALT! DANGER AHEAD! 🛑\n\n" RESET);
            printf(RED "╰─▸ RED SIGNAL! EMERGENCY BRAKES APPLIED! ✋\n");
            printf("╰─▸ There's a cow on the tracks! (Or maybe a sheep?)\n");
            printf("╰─▸ Station master is waving red flags frantically\n\n");
            break;

        case YELLOW_ON_RESPONSE:
            printf(YELLOW BOLD "⚠️  CAUTION! SLOW DOWN! ⚠️\n\n" RESET);
            printf(YELLOW "╰─▸ Yellow alert! Proceed with caution!\n");
            printf("╰─▸ There might be ducks crossing up ahead\n");
            printf("╰─▸ Reduce speed to 'leisurely chug' mode\n\n");
            printf("🚧 Maintenance crew ahead... painting lines?\n" RESET);
            break;

        case RED_BLINK_RESPONSE:
            printf(BRIGHT_RED BOLD "🎆 ATTENTION! BLINKING RED! 🎆\n\n" RESET);
            printf(BRIGHT_RED "╰─▸ WEE-WOO-WEE-WOO! FLASHING LIGHTS!\n");
            printf("╰─▸ Crossing gates are descending!\n");
            printf("╰─▸ Cars waiting, bells ringing!\n\n");
            printf("🎵 *Doppler effect of train horn intensifies*\n" RESET);
            break;

        case EXIT_RESPONSE:
            printf(CYAN BOLD "🚪 END OF THE LINE! 🚪\n\n" RESET);
            printf(CYAN "╰─▸ Conductor: \"Last stop! Everybody out!\"\n");
            printf("╰─▸ Thank you for riding the Code Express!\n");
            printf("╰─▸ Please collect all your bits and bytes\n\n");
            printf(BLUE "🌈 Final destination: Desktop. Have a byte-tiful day!\n" RESET);

            // No animation for EXIT - it should just exit
            printf(BOLD "\n╔══════════════════════════════════════════════════════════╗\n");
            printf("║" BLUE "      [ TRACK " RESET);
            printf(BRIGHT_RED "■" RESET);
    printf(BLUE " STATUS " RESET);
    printf(GREEN "■" RESET);
    printf(BLUE " LIGHTS " RESET);
    printf(YELLOW "■" RESET);
    printf(BLUE " SYSTEMS ]" RESET BOLD "                ║\n");
    printf("╚══════════════════════════════════════════════════════════╝\n\n" RESET);
    return; // Return early, no animation
    break;

default:
    printf(RED "How did we even get here? This train is off the rails! 🚂💨\n" RESET);
    break;
}

printf(BOLD "\n╔══════════════════════════════════════════════════════════╗\n");
printf("║" BLUE "      [ TRACK " RESET);
printf(BRIGHT_RED "■" RESET);
printf(BLUE " STATUS " RESET);
printf(GREEN "■" RESET);
printf(BLUE " LIGHTS " RESET);
printf(YELLOW "■" RESET);
printf(BLUE " SYSTEMS ]" RESET BOLD "               ║\n");
printf("╚══════════════════════════════════════════════════════════╝\n\n" RESET);

// Show the transit light animation for this scenario
if (which != EXIT_RESPONSE) {
    animate_transit_light(which);
}
}



