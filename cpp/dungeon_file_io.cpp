    // The function WriteSceneToFile() writes the content of the world (scene) to a file
    // with name sName:
    // first - the size of the world is written in both width and hight (which gives also
    //         the number of cells in the file)
    // then  - the cells are written, 1 cell per line
    bool WriteSceneToFile( string sName, World &sMap ) {

        // open pointer to outgoing file
        ofstream output_file;
        output_file.open( sName );

        if (!output_file.is_open()) {
            cout << "ERROR - in WriteSceneToFile() --> " << "couldn't open " << sName << endl;
            return false;
        }
        // write world dimensions first
        output_file << sMap.size.x << " " << sMap.size.y << endl;
        // write each cell to file, one per line
        for (int y = 0; y < sMap.size.y; y++) {
            for (int x = 0; x < sMap.size.x; x++) {
                sCell curCell = sMap.GetCell( olc::vi2d{ x, y } );

                output_file << curCell.wall << "     ";
                for (int i = 0; i < 6; i++) {
                    output_file << curCell.id[i].x << " ";
                    output_file << curCell.id[i].y << " ";
                }
                output_file << endl;
            }
        }
        output_file.close();

        return true;
    }

    // NOTE: world (scene) is newly created so if anything was stored in there it gets lost
    bool ReadSceneFromFile( string sName, World &sMap ) {

        // open input file pointer (no error checking)
        ifstream input_file;
        input_file.open( sName );
        if (!input_file.is_open()) {
            cout << "ERROR - in ReadSceneFromFile() --> " << "couldn't open " << sName << endl;
            return false;
        }

        // Read dimensions of the world from file and create world with it
        int nWorldX, nWorldY;
        input_file >> nWorldX >> nWorldY;

        sMap.Create( nWorldX, nWorldY );
        // now populate the scene from the file input
        for (int y = 0; y < nWorldY; y++) {
            for (int x = 0; x < nWorldX; x++) {
                sCell curCell;

                input_file >> curCell.wall;     // read the flag

                for (int j = 0; j < 6; j++)
                    input_file >> curCell.id[j].x >> curCell.id[j].y;   // read indices into sprite sheet per face

                sMap.SetCell( olc::vi2d{ x, y }, curCell );     // copy this info into the world vector of cells
            }
        }
        input_file.close();

        return true;
    }
