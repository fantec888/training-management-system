from pathlib import Path

import fitz


ROOT = Path(__file__).resolve().parents[1]
PDF = Path(r"E:\企业实训\实训文档\开发文档.pdf")
OUT_DIR = ROOT / "docs" / "requirements"
OUT_DIR.mkdir(parents=True, exist_ok=True)
OUT = OUT_DIR / "开发文档-extracted.txt"

doc = fitz.open(PDF)
parts = []
for index, page in enumerate(doc, 1):
    parts.append(f"\n\n===== PAGE {index} =====\n{page.get_text('text')}")

OUT.write_text("".join(parts), encoding="utf-8")
print(OUT)
print("pages", len(doc))
print("chars", sum(len(part) for part in parts))
