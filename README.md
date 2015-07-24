
soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard

soffice --headless --convert-to pdf <file>

https://bugs.documentfoundation.org/show_bug.cgi?id=85777


start /wait "lo" "path to soffice" --headless --convert-to pdf "path to file"

start "lo" "path to soffice" --headless --accept="socket,host=localhost,port=2002;urp;" --nofirststartwizard
