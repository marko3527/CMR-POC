import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { ColumnModel, DropdownModel, RowModel } from '@/common/primary/app/components/valuelist/value-list.model';
import { Button, Col, Row, Table } from 'react-bootstrap';
import { renderInput } from '@/common/primary/app/components/valuelist/value-list-util';
import {
  ColumnDefinitionModel,
  NewSifrarnikColumnType,
} from '@/common/primary/app/components/newsifrarnik/table-definition-model';
const ValueList = () => {

  const [rows, setRows] = useState<RowModel[]>([])
  const [columns, setColumns] = useState<ColumnDefinitionModel[]>([])

  const params = useParams();

  useEffect(() => {
    axios.get(`http://localhost:8080/api/database/values/${params.tableName}`).then(res => {
      setColumns(res.data.columns)
      setRows(res.data.rows)
    })
  }, [])

  const rowChanged = (columnName: string, index: number, newValue: any) => {
    setRows((prevState) => prevState.map((row, rowIndex) => {
      console.log(newValue as DropdownModel)
      if (index === rowIndex) {
        return {
          columns: row.columns.map(column => {
            if (column.columnDefinition.columnName === columnName) {
              return {...column, value: newValue, columnChanged: true}
            }
            return column
          }),
          rowChanged: true
        }
      }
      return row
    }))
  }

  const dodajVrijednostClicked = () => {
    setRows((prevState) => {
      return [...prevState, createEmptyRowWithDefaultValues()]
    })
  }

  const spremiClicked = () => {
    axios.post(`http://localhost:8080/api/database/values/${params.tableName}`, rows).then(res => setRows(res.data.rows))
  }

  const obrisiClicked = (index: number) => {
    const idColumn = rows[index].columns.filter(col => col.columnDefinition.identity)[0]
    if (idColumn.value) {
      axios.post(`http://localhost:8080/api/database/values/${params.tableName}/delete`, rows[index]).then(res => {
        setRows(res.data.rows)
      })
    }
    else {
      setRows((prevState) => {
        return prevState.filter((row, rowIndex) => index !== rowIndex)
      })
    }

  }

  const getId = (index: number) => {
    return rows[index].columns.filter(row => row.columnDefinition.identity)[0].value
  }

  const createEmptyRowWithDefaultValues = () => {
    return {
      columns: columns.map(column => ({
        columnDefinition: column,
        columnChanged: true,
        value: column.defaultValue ?? undefined
      }) as ColumnModel),
      rowChanged: true
    }
  }

  return (
    <>
      <Row>
        <Col md={4}>
          <h3>{params.tableName}</h3>
        </Col>
        <Col md={8} className={"d-flex justify-content-end"}>
          <Button onClick={() => dodajVrijednostClicked()}>Dodaj vrijednost</Button>
          <Button onClick={() => spremiClicked()} variant={'success'}>Spremi</Button>
        </Col>
      </Row>
      <Table striped bordered className={"mt-md-5"}>
        <thead>
        <tr>
          {columns.filter(column => !column.identity).map(column => column.columnName).map(columnName => (
            <th>{columnName}</th>
          ))}
          <th>Akcije</th>
        </tr>
        </thead>
        <tbody>
        {rows.map((row, index) => (
          <tr key={getId(index)}>
            {row.columns.filter(column => !column.columnDefinition.identity).map(column => (
              <td>{renderInput(column.columnDefinition, column.value, index, rowChanged)}</td>
            ))}
            <td><Button variant={'danger'} onClick={() => obrisiClicked(index)}>Obrisi</Button></td>
          </tr>
        ))}
        </tbody>
      </Table>
    </>
  )
}

export default ValueList
