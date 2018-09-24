javac cdac/in/nbe/Allocator.java 
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv  -r 1 -tv > ../pdcet-data/allocation-pdcet.csv
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv  -r 1 -cs > ../pdcet-data/course-status-pdcet.csv
java cdac.in.nbe.Allocator -a ../pdcet-data/pdcet-applicant.csv -c ../pdcet-data/seatmatrix.csv  -r 1 -dnb > ../pdcet-data/pdcet-nbe-view.csv
