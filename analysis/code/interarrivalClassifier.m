
first = [];
second = [];
third = [];
fourth = [];
fifth = [];
sixth = [];
seventh = [];
eighth = [];
nineth = [];
tenth = [];
seventyfive = [];
hundred = [];

for j=1:22809

	if j ==  10267
		continue;
	end
	
	cd(strcat('C:\Users\Ginger\Desktop\finalProject\real\resourceClasses\', int2str(j)));
	
	data = load('interarrivals.txt');
	
	first = [first, data(1)];
	second = [second, data(2)];
	third = [third, data(3)];
	fourth = [fourth, data(4)];
	fifth = [fifth, data(5)];
	sixth = [sixth, data(6)];
	seventh = [seventh, data(7)];
	eighth = [eighth, data(8)];
	nineth = [nineth, data(9)];
	tenth = [tenth, data(10)];
	seventyfive = [seventyfive, data(11)];
	hundred = [hundred, data(12)];
	
end

cd('C:\Documents and Settings\ferhat\Desktop');
save('rfirst.txt', 'first', '-ascii');
save('rsecond.txt', 'second', '-ascii');
save('rthird.txt', 'third', '-ascii');
save('rfourth.txt', 'fourth', '-ascii');
save('rfifth.txt', 'fifth', '-ascii');
save('rsixth.txt', 'sixth', '-ascii');
save('rseventh.txt', 'seventh', '-ascii');
save('reighth.txt', 'eighth', '-ascii');
save('rnineth.txt', 'nineth', '-ascii');
save('rtenth.txt', 'tenth', '-ascii');
save('rseventyfive.txt', 'seventyfive', '-ascii');
save('rhundred.txt', 'hundred', '-ascii');