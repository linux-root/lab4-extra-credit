# ExtraCredit MapReduce Program

A Hadoop MapReduce application that processes weather station temperature data.

## Overview

This program extracts and filters air temperature readings from weather data files, organized by station ID and temperature.

## How It Works

**Mapper:**
- Parses input records and extracts:
  - Station ID (characters 3-14)
  - Air temperature (characters 87-92)
  - Year (characters 15-19)
  - Quality code (character 92)
- Filters out invalid data:
  - Missing values (temperature = 9999)
  - Poor quality readings (quality codes outside "01459")
- Outputs: `(StationID, Temperature)` → `Year`

**Reducer:**
- Passes through all mapper outputs without aggregation

**Custom Key (MyKey):**
- Implements comparable sorting: first by station ID (ascending), then by temperature (descending)

## Usage

```bash
hadoop jar lab4-extra-credit-1.0.jar ExtraCreditMR <input_path> <output_path>
```

## Input Format

Fixed-width weather record format with minimum 93 characters per line.

## Output Format

Tab-separated values: `StationID<TAB>Temperature<TAB>Year`
