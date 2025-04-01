import fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url';

// __dirname equivalent
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// License JSON file path
const licensePath = path.join(__dirname, 'licenses.json');

// Read JSON
const data = await fs.readFile(licensePath, 'utf8');
const licenses = JSON.parse(data);

// Generate Markdown
let output = `# License List\n\n| Package | Version | License | Repository |\n|--------|---------|---------|------------|\n`;

for (const [pkg, info] of Object.entries(licenses)) {
  output += `| ${pkg} | ${info.version} | ${info.licenses} | ${info.repository || ''} |\n`;
}

// Save to file
await fs.writeFile(path.join(__dirname, 'licenses.md'), output);
console.log('✅ licenses.md generated successfully');
