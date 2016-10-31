# reference: http://stackoverflow.com/questions/1417061/automatic-increment-of-build-number-in-qt-creator
Param([string]$targetCompany, [string]$targetProduct, [string]$targetDescription, [string]$targetCopyright)

$source = @"
#pragma once
#define APP_INFO_COMPANY     $targetCompany
#define APP_INFO_PRODUCT     $targetProduct
#define APP_INFO_DESCRIPTION $targetDescription
#define APP_INFO_COPYRIGHT   $targetCopyright

namespace app
{
namespace info
{
static const char* const COMPANY     = $targetCompany;
static const char* const PRODUCT     = $targetProduct;
static const char* const DESCRIPTION = $targetDescription;
static const char* const COPYRIGHT   = $targetCopyright;
}
}
"@
$Utf8NoBomEncoding = New-Object System.Text.UTF8Encoding($False)
[System.IO.File]::WriteAllLines("appinfo.hpp", $source, $Utf8NoBomEncoding)
Write-Host "Application information header generated."