upperLimit = 38477;

classFile = 'class.txt';
mappingFile = 'mapping.txt';

user = [];
resource = [];
 
for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\userClasses\', int2str(i)));
	
	c = load(classFile);
	if c == 2
		m = load(mappingFile);
		user = [user, m(2)];
	end
end
cd('C:\Documents and Settings\ferhat\Desktop\492\plots\classCodes_OverallValues\values');
save('class2users.txt', 'user', '-ascii');

upperLimit = 22809;

for i=1:upperLimit
	cd(strcat('C:\Documents and Settings\ferhat\Desktop\492\plots\resourceClasses\', int2str(i)));
	
	c = load(classFile);
	if c == 3
		m = load(mappingFile);
		resource = [resource, m(2)];
	end
end
cd('C:\Documents and Settings\ferhat\Desktop\492\plots\classCodes_OverallValues\values');
save('class3resources.txt', 'resource', '-ascii');
