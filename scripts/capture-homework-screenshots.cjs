const { chromium } = require('playwright');
const path = require('path');

async function main() {
  const outDir = path.resolve('每日作业/0703作业/screenshots');
  const browser = await chromium.launch({
    headless: true,
    executablePath: 'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe',
  });
  const page = await browser.newPage({ viewport: { width: 1440, height: 960 } });

  await page.goto('http://127.0.0.1:5173/login', { waitUntil: 'networkidle' });
  await page.screenshot({ path: path.join(outDir, '01-login.png'), fullPage: true });

  await page.locator('input[autocomplete="username"]').fill('admin');
  await page.locator('input[autocomplete="current-password"]').fill('123456');
  await page.locator('.login-button').click();
  await page.waitForURL('http://127.0.0.1:5173/', { timeout: 15000 });
  await page.waitForLoadState('networkidle');
  await page.screenshot({ path: path.join(outDir, '02-dashboard.png'), fullPage: true });

  await page.goto('http://127.0.0.1:5173/residents', { waitUntil: 'networkidle' });
  await page.screenshot({ path: path.join(outDir, '03-residents-button-permission.png'), fullPage: true });

  await page.goto('http://127.0.0.1:5173/system-users', { waitUntil: 'networkidle' });
  await page.screenshot({ path: path.join(outDir, '04-rbac-users.png'), fullPage: true });

  const tabFiles = ['05-rbac-roles.png', '06-rbac-permissions.png', '07-rbac-assign.png'];
  const tabs = page.locator('.el-tabs__item');
  for (let i = 1; i <= 3; i += 1) {
    await tabs.nth(i).click();
    await page.waitForTimeout(600);
    await page.screenshot({ path: path.join(outDir, tabFiles[i - 1]), fullPage: true });
  }

  await page.goto('http://127.0.0.1:8080/swagger-ui/index.html', { waitUntil: 'networkidle' });
  await page.waitForTimeout(1500);
  await page.screenshot({ path: path.join(outDir, '08-swagger.png'), fullPage: true });

  await browser.close();
}

main().catch((error) => {
  console.error(error);
  process.exit(1);
});
