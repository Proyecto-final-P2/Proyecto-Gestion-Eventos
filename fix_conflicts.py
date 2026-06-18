import re

def fix_gitignore():
    with open('.gitignore', 'w') as f:
        f.write('target/\n.nanostack/\n.idea/\n')

def replace_between(content, start_marker, end_marker, replacement):
    pattern = re.compile(f"{start_marker}.*?{end_marker}", re.DOTALL)
    return re.sub(pattern, replacement, content)

fix_gitignore()
print("Fixed .gitignore")
