using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Floar : MonoBehaviour
{
    public Vector2Int GridSize = new Vector2Int(10, 10);
    public List<GameObject> buttons;
    private GameObject[,] grid;
    private GameObject selectedObject;
    private Camera mainCamera;
    private float deltaTime = 0.3f;
    bool dragging = false;

    private void Awake()
    {
        grid = new GameObject[GridSize.x, GridSize.y];
        mainCamera = Camera.main;
        foreach (var button in buttons) button.SetActive(false);
    }

    public void CreateNewObject(GameObject furniturePrehub)
    {
        if (selectedObject != null)
            CancelSelection();

        SelectObject(Instantiate(furniturePrehub));
    }

    private void SetObjectToGrid(int placeX, int placeY)
    {
        if (selectedObject == null) return;

        Debug.Log("Here");

        Furniture furniture = selectedObject.GetComponent<Furniture>();
        for (int x = 0; x < furniture.Size.x; x++)
        {
            for (int y = 0; y < furniture.Size.y; y++)
            {
                grid[placeX + x, placeY + y] = selectedObject;
                Debug.Log("Done" + grid[placeX + x, placeY + y]);
            }
        }
    }

    private void DeleteObjectFromGrid(GameObject obj)
    {
        for (int i = 0; i < grid.GetLength(0); i++)
        {
            for (int j = 0; j < grid.GetLength(1); j++)
                if (grid[i, j] == obj) grid[i, j] = null;
        }
    }

    private bool CheckCellsForObject(GameObject obj, int placeX, int placeY)
    {
        Furniture furniture = selectedObject.GetComponent<Furniture>();
        for (int x = 0; x < furniture.Size.x; x++)
        {
            for (int y = 0; y < furniture.Size.y; y++)
            {
                if (grid[placeX + x, placeY + y] != null) return false;
            }
        }
        return true;
    }

    public void CancelSelection()
    {
        var selectedObjectPosition = selectedObject.transform.position;
        if (!CheckCellsForObject(selectedObject, (int)selectedObjectPosition.x, (int)selectedObjectPosition.z)) return;
        Debug.Log(!CheckCellsForObject(selectedObject, (int)selectedObjectPosition.x, (int)selectedObjectPosition.z));
        selectedObject.GetComponent<Outline>().OutlineMode = Outline.Mode.OutlineHidden;
        DeleteObjectFromGrid(selectedObject);
        SetObjectToGrid((int)selectedObjectPosition.x, (int)selectedObjectPosition.z);
        Debug.Log($"{(int)selectedObjectPosition.x}, {(int)selectedObjectPosition.z}");
        selectedObject = null;
        foreach (var button in buttons) button.SetActive(false);
    }

    public void RotateSelectedObject()
    {
        var rotate = Quaternion.Euler(0, (selectedObject.transform.rotation.eulerAngles.y + 90) % 360, 0);
        Debug.Log($"{rotate.x}, {rotate.y}, {rotate.z}");
        selectedObject.transform.rotation = rotate;
    }

    private void SelectObject(GameObject obj)
    {
        foreach (var button in buttons) button.SetActive(true);
        selectedObject = obj;
        var selection = selectedObject.GetComponent<Outline>();
        selection.OutlineMode = Outline.Mode.OutlineVisible;
    }

    void Update()
    {
        if (selectedObject != null)
        {
            var selectedObjectSizeX = selectedObject.GetComponent<Furniture>().Size.x;
            var selectedObjectSizeY = selectedObject.GetComponent<Furniture>().Size.y;
            if (Input.touchCount > 0 && (Input.GetTouch(0).phase == TouchPhase.Began
                || Input.GetTouch(0).phase == TouchPhase.Moved))
            {
                Touch touch = Input.touches[0];
                Vector3 positionOfTouch = touch.position;

                var groundPlane = new Plane(Vector3.up, Vector3.zero);

                Ray ray = mainCamera.ScreenPointToRay(positionOfTouch);
                {
                    if (groundPlane.Raycast(ray, out float position))
                    {
                        Vector3 worldPosition =
                            ((Ray)mainCamera.ScreenPointToRay(positionOfTouch)).GetPoint(position);
                        worldPosition.x = Mathf.RoundToInt(worldPosition.x);
                        worldPosition.z = Mathf.RoundToInt(worldPosition.z);
                        if (worldPosition.x - selectedObjectSizeX < GridSize.x - 1
                            && worldPosition.z - selectedObjectSizeY < GridSize.y - 1
                            && worldPosition.x >= 0 && worldPosition.z >= 0)
                            selectedObject.transform.position = worldPosition;
                    }
                }
            }
        }

        else
        {
            if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Stationary)
            {
                Touch touch = Input.GetTouch(0);
                Ray ray = mainCamera.ScreenPointToRay(touch.position);
                if (Physics.Raycast(ray, out RaycastHit hit))
                {
                    if (hit.collider.gameObject.tag == "FloarFurniture")
                    {
                        if (deltaTime > 0) deltaTime -= Time.deltaTime;
                        else
                        {
                            SelectObject(hit.collider.gameObject);
                            deltaTime = 0.3f;
                        }
                    }
                }
            }
        }
    }
}

