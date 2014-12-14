BusPlzStop
==========

An Android App that notifies the user when getting close to a designated stop

Initial display - Popup (Enter the bus number you are travelling on)
                - Lets say 480
Display - Display all 480 bus stops and the 480 buses with icons on google map(similar to CPSC 210 --Sheldon) 

User selection 1- User click on a bus icon and the program clears away all other bus icons leaving the selected bus on the screen

User selection 2- user click on a bus stop icon and the program clears all bus stops except the ones inbetween the destination stop and the current bus. (The bus stops the bus have to pass) and the destination itself

Program work: The App updates itself every so often (lets do 5 sec interval for testing) and see if the next stop is the destination stop. If it is, it notifies the user. Otherwise it keeps updating itself.

Finish: The user should be able to choose whether to:
             -- stop the program --> program does nothing
             -- repeat if user is multiplatform traveling (i.e. bus-->skytrain-->bus) --> go back to initial display


