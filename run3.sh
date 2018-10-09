javac cdac/in/nbe/Allocator.java 
#java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv -lr ../pdcet-data/pdcet-lastround-applicant.csv -r 3 -rs > ../pdcet-data/vacancy-matrix-pdcet.csv
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv -lr ../pdcet-data/pdcet-lastround-applicant.csv -sp ../pdcet-data/spcCatFile.csv -r 3 -tv > ../pdcet-data/allocation-pdcet.csv
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv -lr ../pdcet-data/pdcet-lastround-applicant.csv -sp ../pdcet-data/spcCatFile.csv -r 3 -cs > ../pdcet-data/course-status-pdcet.csv
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv -lr ../pdcet-data/pdcet-lastround-applicant.csv -sp ../pdcet-data/spcCatFile.csv -r 3 -dnb > ../pdcet-data/nbe-view-pdcet.csv

