mvn clean source:jar install
cd products/tempel-starter/target
mv appasembler/ tempel-0.0.1.RELEASE
chmod +x tempel-0.0.1.RELEASE/bin/tpl

tar -cvzf tempel-0.0.1.RELEASE.tar.gz tempel-0.0.1.RELEASE/
gpg -u kkopacz@agiso.org -b tempel-0.0.1.RELEASE.tar.gz 
md5sum tempel-0.0.1.RELEASE.tar.gz > tempel-0.0.1.RELEASE.tar.gz.md5
gpg --verify tempel-0.0.1.RELEASE.tar.gz.sig tempel-0.0.1.RELEASE.tar.gz

zip -r tempel-0.0.1.RELEASE.zip tempel-0.0.1.RELEASE/
gpg -u kkopacz@agiso.org -b tempel-0.0.1.RELEASE.zip 
md5sum tempel-0.0.1.RELEASE.zip > tempel-0.0.1.RELEASE.zip.md5
gpg --verify tempel-0.0.1.RELEASE.zip.sig tempel-0.0.1.RELEASE.zip
