upperLimit = 38477;

corrFile = 'corr.txt';
diffFile = 'diff.txt';
classFile = 'class.txt';

class1 = 0;
class2 = 0;
class3 = 0;
 
for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\userClasses\', int2str(i)));
	
	c = load(corrFile);
	if c >= 0.95
		class2 = class2 + 1;
		myClass = 2;
	else
		d = load(diffFile);
		if d < 0
			class1 = class1 + 1;
			myClass = 1;
		else
			class3 = class3 + 1;
			myClass = 3;
		end
	end
	save(classFile, 'myClass', '-ascii');
end

cd('C:\Documents and Settings\ferhat\Desktop\');
save('overallClassesNumbersUser.txt', 'class1', 'class2', 'class3', '-ascii');

upperLimit = 22809;

class1 = 0;
class2 = 0;
class3 = 0;

for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\resourceClasses\', int2str(i)));
	
	c = load(corrFile);
	if c >= 0.95
		class2 = class2 + 1;
		myClass = 2;
	else
		d = load(diffFile);
		if d < 0
			class1 = class1 + 1;
			myClass = 1;
		else
			class3 = class3 + 1;
			myClass = 3;
		end
	end
	save(classFile, 'myClass', '-ascii');
end

cd('C:\Documents and Settings\ferhat\Desktop\492\plots\classCodes_OverallValues');
save('overallClassesNumbersResource.txt', 'class1', 'class2', 'class3', '-ascii');